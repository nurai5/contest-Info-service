package kg.mkk.gov.contestinfoservice.contestParser;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum ETag {
    TAG1("КОНКУРСЫ ПО БАТКЕНСКОЙ ОБЛАСТИ"),
    TAG2("КОНКУРСЫ ПО ДЖАЛАЛ АБАДСКОЙОБЛАСТИ"),
    TAG3("КОНКУРСЫ ПО НАРЫНСКОЙ ОБЛАСТИ"),
    TAG4("КОНКУРСЫ ПО ОШСКОЙ ОБЛАСТИ"),
    TAG5("КОНКУРСЫ ПО ТАЛАССКОЙ ОБЛАСТИ"),
    TAG6("КОНКУРСЫ ПО ЦЕНТРАЛЬНЫМ ГОСУДАРСТВЕННЫМ ОРГАНАМ (ПО Г.БИШКЕК)"),
    TAG7("КОНКУРСЫ ПО ЧУЙСКОЙ ОБЛАСТИ"),
    TAG8("КОНКУРСЫ ПО ЫССЫК-КУЛЬСКОЙ ОБЛАСТИ");

    private final String description;

    ETag(String description) {
        this.description = description;
    }
    public static ETag fromDescription(String description) {
        return Arrays.stream(ETag.values())
                .filter(tag -> tag.getDescription().equals(description))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown tag: " + description));
    }
    }
