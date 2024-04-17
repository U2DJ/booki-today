package moim_today.global.constant.exception;

public enum EveryTimeExceptionConstant {

    PARSER_CONFIG_ERROR("XML 문서 파싱 설정에 오류가 있습니다."),
    SAX_IO_ERROR("XML 문서 파싱, 파일 접근에 오류가 발생하였습니다."),
    EVERY_TIME_URL_BAD_REQUEST_ERROR("에브리탕미 시간표 공유 URL이 올바르지 않습니다.");

    private final String value;

    EveryTimeExceptionConstant(final String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
