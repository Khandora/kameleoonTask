package com.kameleoon.test.exception;


import com.kameleoon.test.model.enums.Language;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum ExceptionCode {
    SYSTEM_ERROR(LocalizedMessage.of(Language.RU, "Системная ошибка", Language.KZ, "Системная ошибка", Language.EN, "System error")),
    DATA_ACCESS_ERROR(LocalizedMessage.of(Language.RU, "Ошибка обращения к БД", Language.KZ, "Ошибка обращения к БД", Language.EN, "Data access error")),
    MEMBER_NOT_FOUND(LocalizedMessage.of(Language.RU, "Человек не найден", Language.KZ, "Адам табылған жоқ", Language.EN, "User not found"));

    private final List<LocalizedMessage> localizedMessages;

    ExceptionCode(List<LocalizedMessage> localizedMessages) {
        this.localizedMessages = localizedMessages;
    }

    public List<LocalizedMessage> getLocalizedMessages() {
        return localizedMessages;
    }

    public static class LocalizedMessage implements Serializable {
        private Language language;
        private String message;

        public LocalizedMessage() {
        }

        public Language getLanguage() {
            return language;
        }

        public String getMessage() {
            return message;
        }

        public static List<LocalizedMessage> of(Object... args) {
            if (args.length == 0 || args.length % 2 != 0) {
                throw new IllegalArgumentException();
            }
            List<LocalizedMessage> localizedMessages = new ArrayList<>(args.length / 2);
            for (int i = 0; i < args.length; i += 2) {
                LocalizedMessage lm = new LocalizedMessage();
                lm.language = (Language) args[i];
                lm.message = (String) args[i + 1];
                localizedMessages.add(lm);
            }
            return Collections.unmodifiableList(localizedMessages);
        }
    }

    public String toJSON() {
        StringBuilder sb = new StringBuilder(64);
        sb.append("[");
        int count = 0;
        for (LocalizedMessage lm : this.getLocalizedMessages()) {
            if (count++ > 0) {
                sb.append(',');
            }
            sb.append('{').append("\"lang\":\"").append(lm.getLanguage().name()).append("\",").append("\"message\":\"").append(lm.getMessage()).append("\"").append('}');
        }
        sb.append(']');
        return sb.toString();
    }
}
