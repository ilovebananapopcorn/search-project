package com.sy.keyword.search;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sy.keyword.SearchApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class ControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    void searchControllerTest() throws Exception {

        //------------------키워드 검색 기능 점검------------------

        //테스트케이스 1 : 정상 키워드 검색, 정상 응답
        //{"keyword":"thanks","timestamp":"2023-03-01T21:00:20.5807047","resultList":["땡스북스","Thanks more","땡Thanks","땡스메리","땡스커피","THANKSGOD빌","땡스얼랏","THANKS FATHER","러브앤땡스(Love&amp;Thanks)"]}
        //kakaoapi의 url을 바꿀 시 네이버 5개의 응답만 나옴. (네이버가 맥시멈 5개만 지원함)
        //장애처리를 빈값으로 오게 구현
        mvc.perform(MockMvcRequestBuilders
                .get("/search")
                .param("keyword","thanks")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();


        //테스트케이스 2 : keyword 키값 없음
        //{"errCode":"E99","errMsg":"Required request parameter 'keyword' for method parameter type String is not present"}
        mvc.perform(MockMvcRequestBuilders
                .get("/search")
                .param("hi","thanks")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        //테스트케이스 3 : post method 호출
        //{"errCode":"E03","errMsg":"Request method 'POST' not supported"}
        mvc.perform(MockMvcRequestBuilders
                .post("/search")
                .param("keyword","thanks")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        //테스트케이스 4 : 잘못된Uri 호출
        //Http Status 404
        mvc.perform(MockMvcRequestBuilders
                .post("/search2")
                .param("keyword","thanks")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();


    }
}
