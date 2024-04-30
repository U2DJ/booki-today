package moim_today.presentation.moim;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import moim_today.domain.moim.DisplayStatus;
import moim_today.domain.moim.enums.MoimCategory;
import moim_today.dto.moim.MoimAppendRequest;
import moim_today.dto.moim.MoimUpdateRequest;
import moim_today.fake_class.moim.FakeMoimService;
import moim_today.util.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static moim_today.util.TestConstant.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.JsonFieldType.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MoimControllerTest extends ControllerTest {

    private final FakeMoimService fakeMoimService = new FakeMoimService();

    @Override
    protected Object initController() {
        return new MoimController(fakeMoimService);
    }

    @DisplayName("모임을 생성한다.")
    @Test
    void createPrivateMoimApiTest() throws Exception {
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 6, 30);

        MoimAppendRequest moimAppendRequest = new MoimAppendRequest(
                TITLE.value(),
                CONTENTS.value(),
                Integer.parseInt(CAPACITY.value()),
                PASSWORD.value(),
                MOIM_IMAGE_URL.value(),
                MoimCategory.STUDY,
                DisplayStatus.PRIVATE,
                startDate,
                endDate
        );

        mockMvc.perform(post("/api/moims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moimAppendRequest))
                )
                .andExpect(status().isOk())
                .andDo(document("모임 생성 성공",
                        resource(ResourceSnippetParameters.builder()
                                .tag("모임")
                                .summary("모임 생성")
                                .requestFields(
                                        fieldWithPath("title").type(STRING).description("모임명"),
                                        fieldWithPath("contents").type(STRING).description("내용"),
                                        fieldWithPath("capacity").type(NUMBER).description("모집 인원"),
                                        fieldWithPath("password").type(STRING).description("모임 비밀번호(공개 여부가 PUBLIC일 경우 Nullable)"),
                                        fieldWithPath("imageUrl").type(STRING).description("모임 사진 URL(Nullable)"),
                                        fieldWithPath("moimCategory").type(VARIES).description("카테고리"),
                                        fieldWithPath("displayStatus").type(VARIES).description("공개 여부"),
                                        fieldWithPath("startDate").type(STRING).description("시작 일자"),
                                        fieldWithPath("endDate").type(STRING).description("종료 일자")
                                )
                                .build()
                        )));
    }

    @DisplayName("모임 사진을 업로드/수정하면 업로드/수정된 파일의 URL을 반환한다. ")
    @Test
    void uploadMoimImageTest() throws Exception {

        MockMultipartFile file = new MockMultipartFile(
                FILE_NAME.value(),
                ORIGINAL_FILE_NAME.value(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                FILE_CONTENT.value().getBytes()
        );

        mockMvc.perform(multipart("/api/moims/image")
                        .file(file)
                )
                .andExpect(status().isOk())
                .andDo(document("모임 사진 업로드/수정 성공",
                        requestParts(
                                partWithName("file").description("모임 사진 파일")
                        ),
                        resource(ResourceSnippetParameters.builder()
                                .tag("모임")
                                .summary("모임 사진 업로드/수정")
                                .responseFields(
                                        fieldWithPath("imageUrl").type(STRING).description("모임 사진 URL")
                                )
                                .build()
                        )));
    }

    @DisplayName("모임 정보를 조회한다.")
    @Test
    void getMoimDetailTest() throws Exception {

        mockMvc.perform(get("/api/moims/detail")
                        .param("moimId", "1")
                )
                .andExpect(status().isOk())
                .andDo(document("모임 정보 조회 성공",
                        resource(ResourceSnippetParameters.builder()
                                .tag("모임")
                                .summary("모임 정보 조회")
                                .queryParameters(
                                        parameterWithName("moimId").description("모임 ID")
                                )
                                .responseFields(
                                        fieldWithPath("title").type(STRING).description("모임명"),
                                        fieldWithPath("contents").type(STRING).description("내용"),
                                        fieldWithPath("capacity").type(NUMBER).description("모집 인원"),
                                        fieldWithPath("currentCount").type(NUMBER).description("현재 인원"),
                                        fieldWithPath("imageUrl").type(STRING).description("모임 사진 URL"),
                                        fieldWithPath("moimCategory").type(VARIES).description("카테고리"),
                                        fieldWithPath("displayStatus").type(VARIES).description("공개여부"),
                                        fieldWithPath("views").type(NUMBER).description("조회수"),
                                        fieldWithPath("startDate").type(STRING).description("시작 일자"),
                                        fieldWithPath("endDate").type(STRING).description("종료 일자")
                                )
                                .build()
                        )));
    }

    @DisplayName("모임 정보를 수정한다.")
    @Test
    void updateMoimTest() throws Exception {
        MoimUpdateRequest moimUpdateRequest = MoimUpdateRequest.builder()
                .moimId(Long.parseLong(MOIM_ID.value()))
                .title(TITLE.value())
                .contents(CONTENTS.value())
                .capacity(Integer.parseInt(CAPACITY.value()))
                .imageUrl(MOIM_IMAGE_URL.value())
                .password(PASSWORD.value())
                .moimCategory(MoimCategory.STUDY)
                .displayStatus(DisplayStatus.PRIVATE)
                .startDate(LocalDate.of(2024,3,1))
                .endDate(LocalDate.of(2024,6,30))
                .build();

        mockMvc.perform(patch("/api/moims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(moimUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(document("모임 정보 수정 성공",
                        resource(ResourceSnippetParameters.builder()
                                .tag("모임")
                                .summary("모임 정보 수정")
                                .requestFields(
                                        fieldWithPath("moimId").type(NUMBER).description("수정할 모임의 ID"),
                                        fieldWithPath("title").type(STRING).description("수정한 모임명"),
                                        fieldWithPath("contents").type(STRING).description("수정한 내용"),
                                        fieldWithPath("capacity").type(NUMBER).description("수정한 모집 인원"),
                                        fieldWithPath("imageUrl").type(STRING).description("수정한 모임 사진 URL"),
                                        fieldWithPath("password").type(STRING).description("수정한 비밀번호"),
                                        fieldWithPath("moimCategory").type(VARIES).description("수정한 카테고리"),
                                        fieldWithPath("displayStatus").type(VARIES).description("수정한 공개여부"),
                                        fieldWithPath("startDate").type(STRING).description("수정한 시작일자"),
                                        fieldWithPath("endDate").type(STRING).description("수정한 종료일자")
                                ).build()
                        )));
    }
}
