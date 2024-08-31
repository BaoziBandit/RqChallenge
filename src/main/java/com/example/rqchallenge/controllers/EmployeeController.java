package com.example.rqchallenge.controllers;

import com.example.rqchallenge.clients.DummyRestClient;
import com.example.rqchallenge.models.DummyResponse;
import com.example.rqchallenge.models.Employee;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/employee")
public class EmployeeController implements IEmployeeController {
    private final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);
    private final DummyRestClient client = new DummyRestClient();
    private final Gson gson = new Gson();

    private final String EMPLOYEE = "employee";

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        List<Employee> employees = queryEmployees();
        if(!employees.isEmpty()){
            return ResponseEntity.ok(employees);
        }
        return ResponseEntity.badRequest().body(employees);
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = new ArrayList<>();
        for(Employee employee : queryEmployees()){
            if(employee.getName().toLowerCase().contains(searchString.toLowerCase())){
                employees.add(employee);
            }
        }
        if(!employees.isEmpty()){
            return ResponseEntity.ok(employees);
        }
        return ResponseEntity.badRequest().body(employees);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        String response = client.getById(EMPLOYEE, id);
        if(!response.isEmpty()){
            DummyResponse<Employee> dummyResponse = gson.fromJson(response, new TypeToken<DummyResponse<Employee>>(){}.getType());
            if(dummyResponse.getData() != null){
                return ResponseEntity.ok(dummyResponse.getData());
            }
        } else {
            LOG.error("Failed to get employee with id {}", id);
        }
        return ResponseEntity.badRequest().body(new Employee());
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        List<Employee> employees = queryEmployees();
        if(!employees.isEmpty()){
            int highestSalary = 0;
            for(Employee employee : employees){
                LOG.info("Employee Salary: {}", employee.getSalary());
                int salary = Integer.parseInt(employee.getSalary());
                if(salary > highestSalary){
                    highestSalary = salary;
                }
            }
            return ResponseEntity.ok(highestSalary);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = queryEmployees();
        if(!employees.isEmpty()){
            employees.sort((o1, o2) -> Integer.parseInt(o2.getSalary()) - Integer.parseInt(o1.getSalary()));
            List<String> highestEarningEmployeeNames = new ArrayList<>();
            for(Employee employee : employees){
                highestEarningEmployeeNames.add(employee.getName());
                if(highestEarningEmployeeNames.size() >= 10){
                    break;
                }
            }
            return ResponseEntity.ok(highestEarningEmployeeNames);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<Employee> createEmployee(Map<String, Object> employeeInput) {
        final String NAME = "name";
        final String AGE = "age";
        final String SALARY = "salary";
        String name = String.valueOf(employeeInput.get(NAME));
        String age = String.valueOf(employeeInput.get(AGE));
        String salary = String.valueOf(employeeInput.get(SALARY));
        if(name == null || name.isEmpty() || age == null || age.isEmpty() || salary == null || salary.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        JsonObject object = new JsonObject();
        object.addProperty(NAME, name);
        object.addProperty(AGE, age);
        object.addProperty(SALARY, salary);
        String response = client.create(gson.toJson(object), EMPLOYEE);
        if(!response.isEmpty()){
            DummyResponse<Employee> dummyResponse = gson.fromJson(response, new TypeToken<DummyResponse<Employee>>(){}.getType());
            return ResponseEntity.ok(dummyResponse.getData());
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        if(id == null || id.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        String response = client.delete(id);
        if(!response.isEmpty()){
            return ResponseEntity.ok(response);
        }
        return null;
    }


    private List<Employee> queryEmployees(){
        String response = client.getAll(EMPLOYEE);
        if(!response.isEmpty()){
            DummyResponse<List<Employee>> dummyResponse = gson.fromJson(response, new TypeToken<DummyResponse<List<Employee>>>(){}.getType());
            if(dummyResponse.getData() != null && !dummyResponse.getData().isEmpty()){
                return dummyResponse.getData();
            }
        } else {
            LOG.error("Failed to query employees");
        }
        return new ArrayList<>();
    }
}
