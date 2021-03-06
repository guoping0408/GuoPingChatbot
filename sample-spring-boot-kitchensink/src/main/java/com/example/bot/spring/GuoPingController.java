/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring;

import static java.util.Collections.singletonList;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.client.LineBlobClient;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.FollowEvent;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.UnfollowEvent;
import com.linecorp.bot.model.event.message.LocationMessageContent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.ImagemapMessage;
import com.linecorp.bot.model.message.LocationMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.StickerMessage;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.message.imagemap.ImagemapArea;
import com.linecorp.bot.model.message.imagemap.ImagemapBaseSize;
import com.linecorp.bot.model.message.imagemap.ImagemapExternalLink;
import com.linecorp.bot.model.message.imagemap.ImagemapVideo;
import com.linecorp.bot.model.message.imagemap.URIImagemapAction;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.linecorp.bot.model.response.BotApiResponse;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
public class GuoPingController {
    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Autowired
    private LineBlobClient lineBlobClient;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
        TextMessageContent message = event.getMessage();
        handleTextContent(event.getReplyToken(), event, message);
    }

    @EventMapping
    public void handleLocationMessageEvent(MessageEvent<LocationMessageContent> event) {
        LocationMessageContent locationMessage = event.getMessage();
        reply(event.getReplyToken(), new LocationMessage(
                locationMessage.getTitle(),
                locationMessage.getAddress(),
                locationMessage.getLatitude(),
                locationMessage.getLongitude()
        ));
    }

    @EventMapping
    public void handleUnfollowEvent(UnfollowEvent event) {
        log.info("unfollowed this bot: {}", event);
    }

    @EventMapping
    public void handleFollowEvent(FollowEvent event) {
        String replyToken = event.getReplyToken();
        this.reply(replyToken, Arrays.asList(new StickerMessage("11537", "52002734"),
                new TextMessage("?????????????????????????????????Chatbot???????????????????????????"
                        + "???????????????????????????????????????\n???????????????????????????"),
                new ProfileSupplier().get()
        ));
    }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) {
        String replyToken = event.getReplyToken();
        String data = event.getPostbackContent().getData();
        TemplateMessage templateMessage;
        CarouselTemplate carouselTemplate;
        switch (data) {

            case "menu_home":
                this.reply(replyToken, new ProfileSupplier().get());
                break;

            case "overview":
                carouselTemplate = new CarouselTemplate(
                        Arrays.asList(projectColumn(), internColumn(), campusColumn()
                        ));
                templateMessage = new TemplateMessage("showing the overview", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;

            case "menu_1":
                carouselTemplate = new CarouselTemplate(
                        Arrays.asList(projectColumn()
                        ));
                templateMessage = new TemplateMessage("Showing projects", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;

            case "menu_2":
                carouselTemplate = new CarouselTemplate(
                        Arrays.asList(internColumn()
                        ));
                templateMessage = new TemplateMessage("Showing internship", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;

            case "menu_3":
                carouselTemplate = new CarouselTemplate(
                        Arrays.asList(campusColumn()
                        ));
                templateMessage = new TemplateMessage("Showing campus", carouselTemplate);
                this.reply(replyToken, templateMessage);
                break;

            case "show_face_detection_video":
                this.reply(replyToken, face_video());
                break;

            case "show_car_plate_detection_video":
                this.reply(replyToken, car_plate_video());
                break;

            case "studentIntro":

                TextMessage schoolInfo = new TextMessage("???????????????????????????????????????????????????????????????????????????????????????????????????"
                        + "??????????????????????????????10%?????????????????????????????????????????????????????????????????????"
                        + "?????????????????????????????????????????????");

                StickerMessage studying = new StickerMessage("2", "30");

                TextMessage workInfo = new TextMessage("????????????????????????innodisk?????????????????????????????????????????????????????????????????????????????????????????????"
                        + "???????????????San Diego???ASML?????????????????????????????????EUV????????????software driver???");

                StickerMessage hardworking = new StickerMessage("2", "502");

                this.reply(replyToken,
                        Arrays.asList(schoolInfo, studying, workInfo, hardworking));
                break;

            case "futurePlan":

                TextMessage future = new TextMessage("???????????????????????????CM Lab ?????? Research Intern"
                            + "???????????????????????????????????????????????????????????????????????????\n"
                            + "???????????????????????????????????????????????????????????????????????????????????????");

                StickerMessage GoGoGo = new StickerMessage("1", "114");

                this.reply(replyToken,
                        Arrays.asList(future, GoGoGo));
                break;

            case "Custom NumPy Intro":

                TextMessage numPyIntro = new TextMessage("?????????????????????????????????????????????numpy????????????????????????????????????????????????????????????????????????"
                        + "OpenMP???SIMD instructions ???????????????thread parallelism???data parallelism");
                TextMessage numPyLink = new TextMessage("GitHub ?????????\nhttps://github.com/guoping0408/Custom-Numpy");
                this.reply(replyToken,
                        Arrays.asList(numPyIntro, numPyLink));
                break;

            case "File System Intro":

                TextMessage fileIntro = new TextMessage("??????indexed inode structure??????????????????Unix FFS??????????????????"
                        + "?????????buffer cache????????????????????????, ??????????????????????????????????????????????????????");
                TextMessage fileLink = new TextMessage("GitHub ?????????\nhttps://github.com/guoping0408/OS---file-system");
                this.reply(replyToken,
                        Arrays.asList(fileIntro, fileLink));
                break;

            default:
                this.replyText(replyToken,
                        "Got postback data: " + event.getPostbackContent().getData());
                break;

        }

    }

    private void reply(@NonNull String replyToken, @NonNull Message message) {
        reply(replyToken, singletonList(message));
    }

    private void reply(@NonNull String replyToken, @NonNull List<Message> messages) {
        reply(replyToken, messages, false);
    }

    private void reply(@NonNull String replyToken,
                       @NonNull List<Message> messages,
                       boolean notificationDisabled) {
        try {
            BotApiResponse apiResponse = lineMessagingClient
                    .replyMessage(new ReplyMessage(replyToken, messages, notificationDisabled))
                    .get();
            log.info("Sent messages: {}", apiResponse);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private void replyText(@NonNull String replyToken, @NonNull String message) {
        if (replyToken.isEmpty()) {
            throw new IllegalArgumentException("replyToken must not be empty");
        }
        if (message.length() > 1000) {
            message = message.substring(0, 1000 - 2) + "??????";
        }
        this.reply(replyToken, new TextMessage(message));
    }

    private void handleTextContent(String replyToken, Event event, TextMessageContent content)
            throws Exception {
        final String text = content.getText();

        log.info("Got text message from replyToken:{}: text:{} emojis:{}", replyToken, text,
                content.getEmojis());
        switch (text) {
            case "??????":
                // ?????????????????????
                this.reply(replyToken, new ProfileSupplier().get());
                break;

            case  "????????????":
                //ignored
                break;
            case  "??????????????????":
                //ignored
                break;
            case "??????Custom Numpy????????????":
                //ignored
                break;
            case "??????File System????????????":
                //ignored
                break;

            default:
                this.reply(replyToken,
                        Arrays.asList(new TextMessage("?????????????????????????????????????????????????????????????????????????????????"),
                                new StickerMessage("11539", "52114110")));
                break;

        }
    }

    private static URI createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .scheme("https")
                .path(path).build()
                .toUri();
    }

    private CarouselColumn campusColumn() {
        URI imageUrl = createUri("/static/profile/campus.jpg");
        return new CarouselColumn(imageUrl, "??????", "????????? ?????????", Arrays.asList(
                new PostbackAction("????????????",
                        "studentIntro", "????????????"),
                new URIAction("???????????????",
                        URI.create("https://drive.google.com/file/d/1Zi0IpVGTDng8qnK6ZfmE8tS3K76XtcZV/view?usp=sharing"), null),
                new PostbackAction("??????????????????",
                        "futurePlan", "??????????????????")
        ));
    }

    private CarouselColumn projectColumn() {
        URI imageUrl = createUri("/static/project/robocar.jpg");
        return new CarouselColumn(imageUrl, "Projects", "?????????????????????", Arrays.asList(
                new URIAction("???????????? ????????????App",
                        URI.create("https://youtu.be/SxjZ4XS8Nfk"), null),
                new PostbackAction("???????????? Custom NumPy",
                        "Custom NumPy Intro", "??????Custom Numpy????????????"),
                new PostbackAction("???????????? File System",
                        "File System Intro", "??????File System????????????")
        ));

    }

    private CarouselColumn internColumn() {
        URI imageUrl = createUri("/static/internship_videos3/face.gif");

        return new CarouselColumn(imageUrl, "??????",
                "???innodisk?????????????????????????????????", Arrays.asList(
                new PostbackAction("Demo: ??????????????????",
                        "show_face_detection_video"),
                new PostbackAction("Demo: ??????????????????",
                        "show_car_plate_detection_video"),
                new URIAction("GitHub???????????????????????????",
                        URI.create("https://github.com/guoping0408/AI-application"), null)
        ));
    }

    private ImagemapMessage face_video() {
        return ImagemapMessage
                .builder()
                .baseUrl(createUri("/static/internship_videos3"))
                .altText("This is an imagemap with face detection video")
                .baseSize(new ImagemapBaseSize(722, 1040))
                .video(
                        ImagemapVideo.builder()
                                .originalContentUrl(
                                        createUri("/static/internship_videos3/face_dection.mp4"))
                                .previewImageUrl(
                                        createUri("/static/internship_videos3/face_detection.jpg"))
                                .area(new ImagemapArea(40, 46, 952, 536))
                                .externalLink(
                                        new ImagemapExternalLink(
                                                URI.create("https://github.com/guoping0408/AI-application"),
                                                "GitHub???????????????????????????")
                                )
                                .build()
                )
                .actions(singletonList(
                        URIImagemapAction.builder()
                                .linkUri("https://github.com/guoping0408/AI-application")
                                .area(new ImagemapArea(260, 600, 450, 86))
                                .build()
                ))
                .build();
    }

    private ImagemapMessage car_plate_video() {
        return ImagemapMessage
                .builder()
                .baseUrl(createUri("/static/internship_videos3"))
                .altText("This is an imagemap with car plate detection video")
                .baseSize(new ImagemapBaseSize(722, 1040))
                .video(
                        ImagemapVideo.builder()
                                .originalContentUrl(
                                        createUri("/static/internship_videos3/car_plate_dection.mp4"))
                                .previewImageUrl(
                                        createUri("/static/internship_videos3/car_plate_dection.jpg"))
                                .area(new ImagemapArea(40, 46, 952, 536))
                                .externalLink(
                                        new ImagemapExternalLink(
                                                URI.create("https://github.com/guoping0408/AI-application"),
                                                "GitHub???????????????????????????")
                                )
                                .build()
                )
                .actions(singletonList(
                        URIImagemapAction.builder()
                                .linkUri("https://github.com/guoping0408/AI-application")
                                .area(new ImagemapArea(260, 600, 450, 86))
                                .build()
                ))
                .build();
    }
}
