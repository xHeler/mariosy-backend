package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.interfaces.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MongoEmployeeRepository implements EmployeeRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoEmployeeRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveEmployee(Employee employee) {
        mongoTemplate.save(employee);
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Employee.class));
    }

    @Override
    public List<Employee> findAllEmployeesByFirstName(String firstName) {
        Query query = new Query(Criteria.where("firstName").is(firstName));
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findAllEmployeesByLastName(String lastName) {
        Query query = new Query(Criteria.where("lastName").is(lastName));
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public void updateEmployee(Employee employee) {
        mongoTemplate.save(employee);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        mongoTemplate.remove(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return mongoTemplate.findAll(Employee.class);
    }
}
