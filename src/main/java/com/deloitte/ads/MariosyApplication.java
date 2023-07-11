package com.deloitte.ads;


import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.LocalEmployeeRepository;
import com.deloitte.ads.repositories.LocalMariosRepository;
import com.deloitte.ads.services.EmployeeService;
import com.deloitte.ads.services.MariosService;

public class MariosyApplication
{
    public static void main( String[] args ) {
        EmployeeService employeeService = new EmployeeService(new LocalEmployeeRepository());
        MariosService mariosService = new MariosService(new LocalMariosRepository(), employeeService);
    }
}
