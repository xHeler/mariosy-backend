package com.deloitte.ads.services;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;
import com.deloitte.ads.repositories.interfaces.EmployeeRepository;
import com.deloitte.ads.repositories.interfaces.MariosRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class MariosService {
    private final MariosRepository mariosRepository;
    private final EmployeeService employeeService;

    public void processMariosForEmployee(Employee sender, Employee receiver, String message, ReactionType reaction) throws Exception {
        // check employees exist
        if (!employeeService.isEmployeeExist(sender)) throw new Exception("Employee = " + sender + " not exist!");
        if (!employeeService.isEmployeeExist(receiver)) throw new Exception("Employee = " + receiver + " not exist!");

        Marios marios = Marios.builder().id(1L).message(message).reaction(reaction).sender(sender).receiver(receiver).build();
        saveMarios(marios);
    }

    public void saveMarios(Marios marios) {
        mariosRepository.saveMarios(marios);
    }

    public Marios getMariosById(Long id) throws Exception {
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