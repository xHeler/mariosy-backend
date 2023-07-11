package com.deloitte.ads.services;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;
import com.deloitte.ads.repositories.interfaces.MariosRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class MariosService {
    private final MariosRepository mariosRepository;
    private final EmployeeService employeeService;

    public void addMarios(Employee sender, Employee receiver, String message, ReactionType reaction) throws Exception {
        if (!employeeService.isEmployeeExist(sender)) throw new Exception("Employee = " + sender + " not exist!");
        if (!employeeService.isEmployeeExist(receiver)) throw new Exception("Employee = " + receiver + " not exist!");
        if (sender.equals(receiver)) throw new Exception("You can not give marios for self!");

        Marios marios = Marios.builder().message(message).reaction(reaction).sender(sender).receiver(receiver).build();
        saveMarios(marios);
    }

    public void addMarios(Employee sender, Set<Employee> receivers, String message, ReactionType reaction) {
        //todo: should be in transaction
        receivers.forEach(employee -> {
            try {
                addMarios(sender, employee, message, reaction);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

    public void saveMarios(Marios marios) {
        mariosRepository.saveMarios(marios);
    }

    public Marios getMariosById(UUID id) throws Exception {
        Optional<Marios> mariosOptional = mariosRepository.getMariosById(id);
        if (mariosOptional.isPresent()) return mariosOptional.get();
        throw new Exception("Marios with id=" + id + "not exist!");
    }

    public void updateMarios(Marios marios) {
        mariosRepository.updateMarios(marios);
    }

    public void deleteMarios(Marios marios) {
        mariosRepository.deleteMarios(marios);
    }

    public List<Marios> getAllMarios() {
        return mariosRepository.getAllMarios();
    }
}