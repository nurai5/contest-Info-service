package kg.mkk.gov.contestinfoservice.telegram;

import kg.mkk.gov.contestinfoservice.contestParser.Contest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;


@Component
public class TelegramBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final TelegramClient telegramClient;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.channelId}")
    private String channelId;

    public TelegramBot(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {

    }
    public void sendMessageToChannel(Contest contest) {
        String messageText = String.format("<b>Конкурс</b>\n<b>Дата</b>: %s\n<b>%s</b>\n<b>Описание</b>: %s",
                contest.getDate(), contest.getTitle(), contest.getDescription());

        InlineKeyboardMarkup markup = InlineKeyboardMarkup
                .builder()
                .keyboardRow(
                        new InlineKeyboardRow(InlineKeyboardButton
                                .builder()
                                .text("Подробнее")
                                .url(contest.getUrl())
                                .build())
                )
                .build();

        SendMessage message = SendMessage
                .builder()
                .chatId(channelId)
                .text(messageText)
                .parseMode("HTML")
                .replyMarkup(markup)
                .build();

        try {
            telegramClient.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
