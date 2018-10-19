package com.github.springjwt;

import com.github.springjwt.domain.user.User;
import com.github.springjwt.domain.user.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ConnectControllerTest {
    protected MockMvc mockMvc;
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private Filter springSecurityFilterChain;
    @Autowired
    private UserRepository userRepository;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .defaultRequest(get("/"))
                .build();
    }

    @Test
    public void shouldTestAuthentication() throws Exception {
        createTestUser();

        String result = mockMvc.perform(post("/authentication")
                .param("email", "user@test.pl").param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn().getResponse().getContentAsString();
    }

    private void createTestUser(){
        User user = new User();
        user.setEmail("user@test.pl");
        user.setPassword("5baa61e4c9b93f3f0682250b6cf8331b7ee68fd8"); //password
        user.setUsername("TestUser");
        user.setSalt(null); // no salt needed

        userRepository.save(user);
    }
}