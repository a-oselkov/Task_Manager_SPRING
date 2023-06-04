//package hexlet.code.controller;
//
//import com.github.database.rider.core.api.dataset.DataSet;
//import com.github.database.rider.junit5.api.DBRider;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//
//@SpringBootTest
//// При тестировании можно вообще не запускать сервер
//// Spring будет обрабатывать HTTP запрос и направлять его в контроллер
//// Код вызывается точно так же, как если бы он обрабатывал настоящий запрос
//// Такие тесты обходятся дешевле в плане ресурсов
//// Для этого нужно внедрить MockMvc
//
//// BEGIN
//@AutoConfigureMockMvc
//// END
//
//// Чтобы исключить влияние тестов друг на друга,
//// каждый тест будет выполняться в транзакции.
//// После завершения теста транзакция автоматически откатывается
//@Transactional
//// Для наполнения БД данными перед началом тестирования
//// воспользуемся возможностями библиотеки Database Rider
//@DBRider
//// Файл с данными для тестов (фикстуры)
//@DataSet("users.yml")
//public class UserControllerTest {
//
//    // Автоматическое создание экземпляра класса MockMvc
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    void testRootPage() throws Exception {
//        // Выполняем запрос и получаем ответ
//        MockHttpServletResponse response = mockMvc
//                // Выполняем GET запрос по указанному адресу
//                .perform(get("/welcome"))
//                // Получаем результат MvcResult
//                .andReturn()
//                // Получаем ответ MockHttpServletResponse из класса MvcResult
//                .getResponse();
//
//        // Проверяем статус ответа
//        assertThat(response.getStatus()).isEqualTo(200);
//        // Проверяем, что ответ содержит определенный текст
//        assertThat(response.getContentAsString()).contains("Welcome to Spring");
//    }
//
//    @Test
//    void testGetUsers() throws Exception {
//        MockHttpServletResponse response = mockMvc
//                .perform(get("/api/users"))
//                .andReturn()
//                .getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(200);
//        // Проверяем, что тип содержимого в ответе JSON
//        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
//        // Проверяем, что тело ответа содержит данные сущностей
//        assertThat(response.getContentAsString()).contains("John", "Smith");
//        assertThat(response.getContentAsString()).contains("Jack", "Doe");
//    }
//
//    @Test
//    void testGetUser() throws Exception {
//        MockHttpServletResponse response = mockMvc
//                .perform(get("/api/users/1"))
//                .andReturn()
//                .getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(200);
//        // Проверяем, что тип содержимого в ответе JSON
//        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
//        // Проверяем, что тело ответа содержит данные сущностей
//        assertThat(response.getContentAsString()).contains("John", "Smith");
//    }
//
//    @Test
//    void testCreateUser() throws Exception {
//        MockHttpServletResponse responsePost = mockMvc
//                .perform(
//                        // Выполняем POST-запрос
//                        post("/api/users")
//                                // Устанавливаем тип содержимого тела запроса
//                                .contentType(MediaType.APPLICATION_JSON)
//                                // Добавляем содержимое тела
//                                .content("{\"firstName\": \"Jackson\", \"lastName\": \"Bind\", "
//                                        + "\"email\": \"jackson@gmail.com\", \"password\": \"12345\"}")
//                )
//                .andReturn()
//                .getResponse();
//
//        assertThat(responsePost.getStatus()).isEqualTo(200);
//
//        // Проверяем, что сущность добавилась в базу
//        // Выполняем GET-запрос на страницу вывода всех сущностей
//        MockHttpServletResponse response = mockMvc
//                .perform(get("/api/users"))
//                .andReturn()
//                .getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(200);
//        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
//        // Проверяем, что созданная сущность появилась в базе
//        assertThat(response.getContentAsString()).contains("Jackson", "Bind");
//    }
//
//    // BEGIN
//
//    @Test
//    void testUpdateUser() throws Exception {
//        long id = 1;
//        MockHttpServletResponse responsePost = mockMvc
//                .perform(
//                        // Выполняем PUT-запрос
//                        put("/api/users/" + id)
//                                // Устанавливаем тип содержимого тела запроса
//                                .contentType(MediaType.APPLICATION_JSON)
//                                // Добавляем содержимое тела
//                                .content("{\"firstName\": \"John\", \"lastName\": \"S\", "
//                                        + "\"email\": \"john@gmail.com\", \"password\": \"12345\"}")
//                )
//                .andReturn()
//                .getResponse();
//
//        assertThat(responsePost.getStatus()).isEqualTo(200);
//
//        // Проверяем, что сущность была обновлена
//        // Выполняем GET-запрос на страницу вывода всех сущностей
//        MockHttpServletResponse response = mockMvc
//                .perform(get("/api/users/" + id))
//                .andReturn()
//                .getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(200);
//        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
//        // Проверяем, что созданная сущность появилась в базе
//        assertThat(response.getContentAsString()).contains("John", "S");
//    }
//
//    @Test
//    void testDeletePerson() throws Exception {
//        long id = 1;
//        MockHttpServletResponse responsePost = mockMvc
//                .perform(
//                        // Выполняем DELETE-запрос
//                        delete("/api/users/" + id)
//                )
//                .andReturn()
//                .getResponse();
//
//        assertThat(responsePost.getStatus()).isEqualTo(200);
//
//        // Проверяем, что сущность была удалена из базы
//        // Выполняем GET-запрос на страницу вывода всех сущностей
//        MockHttpServletResponse response = mockMvc
//                .perform(get("/api/users"))
//                .andReturn()
//                .getResponse();
//
//        assertThat(response.getStatus()).isEqualTo(200);
//        assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON.toString());
//        // Проверяем, что сущности нет
//        assertThat(response.getContentAsString()).doesNotContain("John", "Smith");
//    }
//    // END
//}
