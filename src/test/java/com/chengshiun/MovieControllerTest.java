package com.chengshiun;

import org.apache.coyote.Request;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getMovies_normalMember_success() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/getMovies")
                .with(httpBasic("normal@gmail.com", "normal"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));
    }

    @Test
    public void watchFreeMovie_normalMember_or_admin_success() throws Exception {
        //normal_member
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/watchFreeMovie")
                .with(httpBasic("normal@gmail.com", "normal"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));

        //admin
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/watchFreeMovie")
                .with(httpBasic("admin@gmail.com", "admin"));

        mockMvc.perform(requestBuilder2)
                .andExpect(status().is(200));

    }

    @Test
    public void watchVipMovie_vipMember_or_admin_success() throws Exception {
        //vip_member
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/watchVipMovie")
                .with(httpBasic("vip@gmail.com", "vip"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));

        //admin
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/watchVipMovie")
                .with(httpBasic("admin@gmail.com", "admin"));

        mockMvc.perform(requestBuilder2)
                .andExpect(status().is(200));

    }

    @Test
    public void watchVipMovie_normalMember_fail() throws Exception {
        //normal_member
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/watchVipMovie")
                .with(httpBasic("normal@gmail.com", "normal"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    public void uploadMovie_normalMember_fail() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/uploadMovie")
                .with(httpBasic("normal@gmail.com", "normal"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));
    }

    @Test
    public void uploadMovie_movieManager_or_admin_success() throws Exception {
        //movie_manager
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/uploadMovie")
                .with(httpBasic("movie-manager@gmail.com", "movie-manager"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));

        //admin
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .post("/uploadMovie")
                .with(httpBasic("admin@gmail.com", "admin"));

        mockMvc.perform(requestBuilder2)
                .andExpect(status().is(200));
    }

    @Test
    public void deleteMovie_MovieManager_or_admin_success() throws Exception {
        //movie-manager
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/deleteMovie")
                .with(httpBasic("movie-manager@gmail.com", "movie-manager"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(200));

        //admin
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .delete("/deleteMovie")
                .with(httpBasic("admin@gmail.com", "admin"));

        mockMvc.perform(requestBuilder2)
                .andExpect(status().is(200));
    }

    @Test
    public void deleteMovie_normalMember_or_vipMember_fail() throws Exception {
        //normal_member
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/deleteMovie")
                .with(httpBasic("normal@gmail.com", "normal"));

        mockMvc.perform(requestBuilder)
                .andExpect(status().is(403));

        //vip_member
        RequestBuilder requestBuilder2 = MockMvcRequestBuilders
                .delete("/deleteMovie")
                .with(httpBasic("vip@gmail.com", "vip"));

        mockMvc.perform(requestBuilder2)
                .andExpect(status().is(403));
    }
}
