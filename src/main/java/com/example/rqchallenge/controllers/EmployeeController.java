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
    private final DummyRestClient client;
    private final Gson gson = new Gson();

    public EmployeeController(){
        this(null);
    }
    public EmployeeController(DummyRestClient client){
        this.client = client != null ? client : new DummyRestClient();
    }

    private final String EMPLOYEE = "employee";

    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        List<Employee> employees = queryEmployees();
        if(employees != null) {
            LOG.info("Received {} employees ", employees.size());
            return ResponseEntity.ok(employees);
        }
        return ResponseEntity.unprocessableEntity().build();
    }

    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = queryEmployees();
        if(employees == null){
            return ResponseEntity.unprocessableEntity().body(null);
        }
        List<Employee> employeesByName = new ArrayList<>();
        for(Employee employee : employees){
            if(employee.getName().toLowerCase().contains(searchString.toLowerCase())){
                employeesByName.add(employee);
            }
        }
        if(!employeesByName.isEmpty()){
            LOG.info("Received {} employees with name {}", employees.size(), searchString);
            return ResponseEntity.ok(employeesByName);
        }
        return ResponseEntity.badRequest().body(employeesByName);
    }

    @Override
    public ResponseEntity<Employee> getEmployeeById(String id) {
        String response = client.getById(EMPLOYEE, id);
        if(response != null){
            try{
                DummyResponse<Employee> dummyResponse = gson.fromJson(response, new TypeToken<DummyResponse<Employee>>(){}.getType());
                if(dummyResponse.getData() != null){
                    LOG.info("Found employee with id {}", id);
                    return ResponseEntity.ok(dummyResponse.getData());
                }
            } catch (Exception e){
                LOG.error("Failed to get employee by id {}: {}", id, e.getLocalizedMessage());
            }
        } else {
            LOG.error("Error received retrieving Employee {}", id);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        List<Employee> employees = queryEmployees();
        if(employees == null){
            return ResponseEntity.unprocessableEntity().body(null);
        }
        if(!employees.isEmpty()){
            int highestSalary = 0;
            for(Employee employee : employees){
                int salary = Integer.parseInt(employee.getSalary());
                if(salary > highestSalary){
                    highestSalary = salary;
                }
            }
            LOG.info("Highest employee salary is {}", highestSalary);
            return ResponseEntity.ok(highestSalary);
        }
        return ResponseEntity.badRequest().body(null);
    }

    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = queryEmployees();
        if(employees != null){
            if(employees.isEmpty()){
                return ResponseEntity.badRequest().body(new ArrayList<>());
            }
            employees.sort((o1, o2) -> Integer.parseInt(o2.getSalary()) - Integer.parseInt(o1.getSalary()));
            List<String> highestEarningEmployeeNames = new ArrayList<>();
            for(Employee employee : employees){
                highestEarningEmployeeNames.add(employee.getName());
                if(highestEarningEmployeeNames.size() >= 10){
                    break;
                }
            }
            LOG.info("Found top {} highest salary employees", highestEarningEmployeeNames.size());
            return ResponseEntity.ok(highestEarningEmployeeNames);
        }
        return ResponseEntity.unprocessableEntity().body(null);
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
        String response = client.create(object.toString());
        if(response != null){
            try{
                DummyResponse<Map<String, Object>> dummyResponse = gson.fromJson(response, new TypeToken<DummyResponse<Map<String, Object>>>(){}.getType());
                Employee employee = extractEmployee(dummyResponse.getData());
                LOG.info("Created employee with id {} successfully", employee.getId());
                return ResponseEntity.ok(employee);
            } catch (Exception e){
                LOG.error("Failed to create employee {}: {}", name, e.getLocalizedMessage());
            }
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }

    @Override
    public ResponseEntity<String> deleteEmployeeById(String id) {
        if(id == null || id.isEmpty()){
            return ResponseEntity.badRequest().body(null);
        }
        String response = client.delete(id);
        if(response != null){
            LOG.info("Deleted employee with id {}", id);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.unprocessableEntity().body(null);
    }


    private Employee extractEmployee(Map<String, Object> data) {
        Employee employee = new Employee();
        if(data.containsKey("name")){
            employee.setName(String.valueOf(data.get("name")));
        }
        if(data.containsKey("salary")){
            employee.setSalary(String.valueOf(data.get("salary")));
        }
        if(data.containsKey("age")){
            employee.setAge(String.valueOf(data.get("age")));
        }
        if(data.containsKey("id")){
            int id = (int)Double.parseDouble(String.valueOf(data.get("id")));
            employee.setId(String.valueOf(id));
        }
        return employee;
    }

    private List<Employee> queryEmployees(){
        List<Employee> results = null;
        String response = client.getAll(EMPLOYEE);
        if(response != null){
            try{
                DummyResponse<List<Employee>> dummyResponse = gson.fromJson(response, new TypeToken<DummyResponse<List<Employee>>>(){}.getType());
                if(dummyResponse.getData() != null && !dummyResponse.getData().isEmpty()){
                    results = dummyResponse.getData();
                }
            } catch (Exception e){
                LOG.error("Failed to query employees: {}", e.getLocalizedMessage());
            }
        } else {
            LOG.error("Employees query unsuccessful.");
        }
        return results;
    }
}
