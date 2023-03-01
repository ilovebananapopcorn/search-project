package com.sy.keyword.search;


import com.sy.keyword.biz.search.service.HistoryService;
import com.sy.keyword.common.data.repository.KeywordHistoryRepository;
import com.sy.keyword.common.data.repository.StatisticsRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private HistoryService historyService;

    @Autowired
    private StatisticsRepository statisticsRepository;

    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach
    public void setup(){
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @Transactional
    void searchControllerTest() throws Exception {
        //초기 데이터 적재
        for(int idx=0; idx < 10 ;idx++) {
            mvc.perform(MockMvcRequestBuilders
                    .get("/search")
                    .param("keyword", "thanks")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }
        for(int idx=0; idx < 20 ;idx++) {
            mvc.perform(MockMvcRequestBuilders
                    .get("/search")
                    .param("keyword", "hi")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }
        for(int idx=0; idx < 20 ;idx++) {
            mvc.perform(MockMvcRequestBuilders
                    .get("/search")
                    .param("keyword", "은행"+idx)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print());
        }

        //미리 통계내기
        historyService.giveStatistics();

        //------------------키워드 검색 기능 점검------------------

        //테스트케이스 1 : 통계 치 있으며, 10개 정상 응답
        //{"timestamp":"2023-03-01T22:08:29.2582172","keywords":[{"keyword":"hi","searchCount":20},{"keyword":"thanks","searchCount":10},{"keyword":"은행2","searchCount":1},{"keyword":"은행3","searchCount":1},{"keyword":"은행4","searchCount":1},{"keyword":"은행5","searchCount":1},{"keyword":"은행6","searchCount":1},{"keyword":"은행0","searchCount":1},{"keyword":"은행9","searchCount":1},{"keyword":"은행18","searchCount":1}]}
        mvc.perform(MockMvcRequestBuilders
                .get("/rank")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();



        statisticsRepository.deleteToday("20230301");

        //테스트케이스 2 : 통계치 없음
        //{"timestamp":"2023-03-01T22:08:29.2824999","keywords":[]}
        mvc.perform(MockMvcRequestBuilders
                .get("/rank")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();

        //테스트케이스 3 : post method 호출
        //{"errCode":"E03","errMsg":"Request method 'POST' not supported"}
        mvc.perform(MockMvcRequestBuilders
                .post("/rank")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andReturn();



    }
}
