/*
 * Copyright 2018 LINE Corporation
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

import static java.util.Arrays.asList;

import java.net.URI;
import java.util.function.Supplier;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectRatio;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextStyle;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;

public class ProfileSupplier implements Supplier<FlexMessage> {
    @Override
    public FlexMessage get() {
        final Image heroBlock =
                Image.builder()
                        .url(createUri("/static/profile/headshot2.jpg"))
                        .size(ImageSize.FULL_WIDTH)
                        .aspectRatio(ImageAspectRatio.R20TO13)
                        .aspectMode(ImageAspectMode.Cover)
                        .action(new URIAction("label", URI.create("https://drive.google.com/file/d/1UQcyns3luQrKYkQg9a5uLFW0R3pvohpq/view?usp=sharing"), null))
                        .build();

        final Box bodyBlock = createBodyBlock();
        final Box footerBlock = createFooterBlock();
        final Bubble bubble =
                Bubble.builder()
                        .hero(heroBlock)
                        .body(bodyBlock)
                        .footer(footerBlock)
                        .build();

        return new FlexMessage("Showing the profile page", bubble);
    }

    private Box createFooterBlock() {
        final Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
        final Button callAction = Button
                .builder()
                .style(ButtonStyle.LINK)
                .height(ButtonHeight.SMALL)
                .action(new URIAction("CALL", URI.create("tel:0905929577"), null))
                .build();
        final Separator separator = Separator.builder().build();
        final Button emailAction =
                Button.builder()
                        .style(ButtonStyle.LINK)
                        .height(ButtonHeight.SMALL)
                        .action(new URIAction("EMAIL", URI.create("mailto:stephenhung0408@berkeley.edu"), null))
                        .build();

        final Box contact =
                Box.builder()
                        .layout(FlexLayout.HORIZONTAL)
                        .spacing(FlexMarginSize.SM)
                        .contents(asList(callAction, separator, emailAction))
                        .build();

        final Button resumeAction =
                Button.builder()
                        .style(ButtonStyle.LINK)
                        .height(ButtonHeight.SMALL)
                        .action(new URIAction("履歷PDF", URI.create("https://drive.google.com/file/d/1UQcyns3luQrKYkQg9a5uLFW0R3pvohpq/view?usp=sharing"), null))
                        .build();
        final Button linkedInAction =
                Button.builder()
                        .style(ButtonStyle.LINK)
                        .height(ButtonHeight.SMALL)
                        .action(new URIAction("LinkedIn", URI.create("https://www.linkedin.com/in/guopinghung/"), null))
                        .build();

        final Box contact2 =
                Box.builder()
                        .layout(FlexLayout.HORIZONTAL)
                        .spacing(FlexMarginSize.SM)
                        .contents(asList(linkedInAction, separator, resumeAction))
                        .build();

        final Button moreAction =
                Button.builder()
                        .style(ButtonStyle.PRIMARY)
                        .height(ButtonHeight.SMALL)
                        .action(new PostbackAction("實作、實習、校園總覽",
                                "overview"))
                        .build();

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .spacing(FlexMarginSize.SM)
                .contents(asList(spacer, contact, separator, contact2, separator, moreAction))
                .build();
    }

    private Box createBodyBlock() {
        final Text title =
                Text.builder()
                        .text("洪國評  Guo-Ping Hung")
                        .weight(TextWeight.BOLD)
                        .size(FlexFontSize.XL)
                        .build();

        final Box info = createInfoBox();

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .contents(asList(title, info))
                .build();
    }

    private Box createInfoBox() {
        final Box MS = Box
                .builder()
                .layout(FlexLayout.BASELINE)
                .spacing(FlexMarginSize.SM)
                .contents(asList(
                        Text.builder()
                                .text("M.S. in CSIE,")
                                .color("#666666")
                                .size(FlexFontSize.SM)
                                .style(TextStyle.ITALIC)
                                .flex(1)
                                .build(),
                        Text.builder()
                                .text("National Taiwan University")
                                .wrap(true)
                                .color("#666666")
                                .size(FlexFontSize.SM)
                                .flex(2)
                                .build()
                ))
                .build();
        final Box BS =
                Box.builder()
                        .layout(FlexLayout.BASELINE)
                        .spacing(FlexMarginSize.SM)
                        .contents(asList(
                                Text.builder()
                                        .text("B.S. in EECS,")
                                        .color("#666666")
                                        .size(FlexFontSize.SM)
                                        .style(TextStyle.ITALIC)
                                        .flex(1)
                                        .build(),
                                Text.builder()
                                        .text("UC Berkeley, 3.89/4")
                                        .wrap(true)
                                        .color("#666666")
                                        .size(FlexFontSize.SM)
                                        .flex(2)
                                        .build()
                        ))
                        .build();

        return Box.builder()
                .layout(FlexLayout.VERTICAL)
                .margin(FlexMarginSize.LG)
                .spacing(FlexMarginSize.SM)
                .contents(asList(MS, BS))
                .build();
    }

    private static URI createUri(String path) {
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .scheme("https")
                .path(path).build()
                .toUri();
    }
}
