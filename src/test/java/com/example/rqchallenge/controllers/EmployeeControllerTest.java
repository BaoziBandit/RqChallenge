package com.example.rqchallenge.controllers;

import com.example.rqchallenge.clients.DummyRestClient;
import com.example.rqchallenge.models.Employee;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@SpringBootTest
public class EmployeeControllerTest {

    private final DummyRestClient drc = Mockito.mock(DummyRestClient.class);
    private final EmployeeController ec = new EmployeeController(drc);

    @BeforeEach
    void setUp() {
        String EMPLOYEE = "employee";
        when(drc.getAll(EMPLOYEE)).thenReturn(mockListDataString());
        when(drc.getById(EMPLOYEE, "14")).thenReturn(mockObjectDataString());
    }

    @Test
    public void getAllEmployeesTest(){
        try{


            ResponseEntity<List<Employee>> data = ec.getAllEmployees();
            Assertions.assertThat(data).isNotNull();
            Assertions.assertThat(data.getBody()).isNotNull();
            List<Employee> employees = data.getBody();
            Assertions.assertThat(employees).isNotNull();
            Assertions.assertThat(employees).isNotEmpty();
            Assertions.assertThat(employees.size()).isEqualTo(24);
        } catch (IOException e) {
            Assertions.fail("IOException thrown");
        }
    }

    @Test
    public void getEmployeeByIdTest(){
        ResponseEntity<Employee> data = ec.getEmployeeById("14");
        Assertions.assertThat(data).isNotNull();
        Assertions.assertThat(data.getBody()).isNotNull();

        Employee employee = data.getBody();
        Assertions.assertThat(employee).isNotNull();
        Assertions.assertThat(employee.getId()).isEqualTo("14");
        Assertions.assertThat(employee.getName()).isEqualTo("Haley Kennedy");
        Assertions.assertThat(employee.getSalary()).isEqualTo("313500");
        Assertions.assertThat(employee.getAge()).isEqualTo("43");
    }

    @Test
    public void getEmployeeByNameSearchTest(){
        ResponseEntity<List<Employee>> data = ec.getEmployeesByNameSearch("Haley");
        Assertions.assertThat(data).isNotNull();
        Assertions.assertThat(data.getBody()).isNotNull();

        List<Employee> employees = data.getBody();
        Assertions.assertThat(employees).isNotNull();
        Assertions.assertThat(employees).isNotEmpty();
        Employee employee = employees.get(0);
        Assertions.assertThat(employee.getId()).isEqualTo("14");
        Assertions.assertThat(employee.getName()).isEqualTo("Haley Kennedy");
        Assertions.assertThat(employee.getSalary()).isEqualTo("313500");
        Assertions.assertThat(employee.getAge()).isEqualTo("43");
    }

    @Test
    public void getHighestSalaryOfEmployees(){
        ResponseEntity<Integer> data = ec.getHighestSalaryOfEmployees();
        Assertions.assertThat(data).isNotNull();
        Assertions.assertThat(data.getBody()).isNotNull();
        Assertions.assertThat(data.getBody()).isEqualTo(725000);
    }

    @Test
    public void getHighestEarningEmployeeNamesTest(){
        ResponseEntity<List<String>> data = ec.getTopTenHighestEarningEmployeeNames();
        Assertions.assertThat(data).isNotNull();
        Assertions.assertThat(data.getBody()).isNotNull();
        Assertions.assertThat(data.getBody()).isNotEmpty();
        List<String> names = data.getBody();
        Assertions.assertThat(names.get(0)).isEqualTo("Paul Byrd");
        Assertions.assertThat(names.get(names.size() - 1)).isEqualTo("Tiger Nixon");
    }

    private String mockObjectDataString(){
        return "{\n" +
                "  \"status\": \"success\",\n" +
                "  \"data\": {\n" +
                "      \"id\": 14,\n" +
                "      \"employee_name\": \"Haley Kennedy\",\n" +
                "      \"employee_salary\": 313500,\n" +
                "      \"employee_age\": 43,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "  \"message\": \"Successfully! All records has been fetched.\"\n" +
                "}";
    }

    private String mockListDataString(){
        return "{\n" +
                "  \"status\": \"success\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"employee_name\": \"Tiger Nixon\",\n" +
                "      \"employee_salary\": 320800,\n" +
                "      \"employee_age\": 61,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 2,\n" +
                "      \"employee_name\": \"Garrett Winters\",\n" +
                "      \"employee_salary\": 170750,\n" +
                "      \"employee_age\": 63,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 3,\n" +
                "      \"employee_name\": \"Ashton Cox\",\n" +
                "      \"employee_salary\": 86000,\n" +
                "      \"employee_age\": 66,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 4,\n" +
                "      \"employee_name\": \"Cedric Kelly\",\n" +
                "      \"employee_salary\": 433060,\n" +
                "      \"employee_age\": 22,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 5,\n" +
                "      \"employee_name\": \"Airi Satou\",\n" +
                "      \"employee_salary\": 162700,\n" +
                "      \"employee_age\": 33,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 6,\n" +
                "      \"employee_name\": \"Brielle Williamson\",\n" +
                "      \"employee_salary\": 372000,\n" +
                "      \"employee_age\": 61,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 7,\n" +
                "      \"employee_name\": \"Herrod Chandler\",\n" +
                "      \"employee_salary\": 137500,\n" +
                "      \"employee_age\": 59,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 8,\n" +
                "      \"employee_name\": \"Rhona Davidson\",\n" +
                "      \"employee_salary\": 327900,\n" +
                "      \"employee_age\": 55,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 9,\n" +
                "      \"employee_name\": \"Colleen Hurst\",\n" +
                "      \"employee_salary\": 205500,\n" +
                "      \"employee_age\": 39,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 10,\n" +
                "      \"employee_name\": \"Sonya Frost\",\n" +
                "      \"employee_salary\": 103600,\n" +
                "      \"employee_age\": 23,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 11,\n" +
                "      \"employee_name\": \"Jena Gaines\",\n" +
                "      \"employee_salary\": 90560,\n" +
                "      \"employee_age\": 30,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 12,\n" +
                "      \"employee_name\": \"Quinn Flynn\",\n" +
                "      \"employee_salary\": 342000,\n" +
                "      \"employee_age\": 22,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 13,\n" +
                "      \"employee_name\": \"Charde Marshall\",\n" +
                "      \"employee_salary\": 470600,\n" +
                "      \"employee_age\": 36,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 14,\n" +
                "      \"employee_name\": \"Haley Kennedy\",\n" +
                "      \"employee_salary\": 313500,\n" +
                "      \"employee_age\": 43,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 15,\n" +
                "      \"employee_name\": \"Tatyana Fitzpatrick\",\n" +
                "      \"employee_salary\": 385750,\n" +
                "      \"employee_age\": 19,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 16,\n" +
                "      \"employee_name\": \"Michael Silva\",\n" +
                "      \"employee_salary\": 198500,\n" +
                "      \"employee_age\": 66,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 17,\n" +
                "      \"employee_name\": \"Paul Byrd\",\n" +
                "      \"employee_salary\": 725000,\n" +
                "      \"employee_age\": 64,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 18,\n" +
                "      \"employee_name\": \"Gloria Little\",\n" +
                "      \"employee_salary\": 237500,\n" +
                "      \"employee_age\": 59,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 19,\n" +
                "      \"employee_name\": \"Bradley Greer\",\n" +
                "      \"employee_salary\": 132000,\n" +
                "      \"employee_age\": 41,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 20,\n" +
                "      \"employee_name\": \"Dai Rios\",\n" +
                "      \"employee_salary\": 217500,\n" +
                "      \"employee_age\": 35,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 21,\n" +
                "      \"employee_name\": \"Jenette Caldwell\",\n" +
                "      \"employee_salary\": 345000,\n" +
                "      \"employee_age\": 30,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 22,\n" +
                "      \"employee_name\": \"Yuri Berry\",\n" +
                "      \"employee_salary\": 675000,\n" +
                "      \"employee_age\": 40,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 23,\n" +
                "      \"employee_name\": \"Caesar Vance\",\n" +
                "      \"employee_salary\": 106450,\n" +
                "      \"employee_age\": 21,\n" +
                "      \"profile_image\": \"\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"id\": 24,\n" +
                "      \"employee_name\": \"Doris Wilder\",\n" +
                "      \"employee_salary\": 85600,\n" +
                "      \"employee_age\": 23,\n" +
                "      \"profile_image\": \"\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"message\": \"Successfully! All records has been fetched.\"\n" +
                "}";
    }
}
