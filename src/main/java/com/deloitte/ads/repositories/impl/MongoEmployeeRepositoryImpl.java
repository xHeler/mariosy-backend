package com.deloitte.ads.repositories.impl;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Repository
@Profile("mongo")
public class MongoEmployeeRepositoryImpl implements EmployeeRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoEmployeeRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveEmployee(Employee employee) {
        mongoTemplate.save(employee);
    }

    @Override
    public Optional<Employee> getEmployeeById(UUID id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Employee.class));
    }

    @Override
    public List<Employee> findAllEmployeesByFirstName(String firstName) {
        String regex = ".*" + firstName.toLowerCase() + ".*";
        Query query = new Query(Criteria.where("firstName").regex(Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public List<Employee> findAllEmployeesByLastName(String lastName) {
        String regex = ".*" + lastName.toLowerCase() + ".*";
        Query query = new Query(Criteria.where("lastName").regex(Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
        return mongoTemplate.find(query, Employee.class);
    }

    @Override
    public boolean isEmployeeWithEmailExist(String email) {
        Query query = new Query(Criteria.where("email").is(email));
        return mongoTemplate.exists(query, Employee.class);
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
