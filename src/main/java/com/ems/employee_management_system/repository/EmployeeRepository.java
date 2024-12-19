package com.ems.employee_management_system.repository;

import com.ems.employee_management_system.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
public class EmployeeRepository {
    @Autowired
    private JdbcTemplate template;

    //insert new employee
    public int insertEmployee(Employee employee) {
        String sql = "SELECT insert_emp(?, ?, ?, ?, ?)";
        try {
            return template.queryForObject(sql,
                    new Object[]{
                            employee.getId(),
                            employee.getFirstName(),
                            employee.getLastName(),
                            employee.getEmail(),
                            employee.getJob_title()
                    },
                    Integer.class
            );
        } catch (DataAccessException e) {
            // Handle exception, such as duplicate ID
            throw new RuntimeException("Error inserting employee: " + e.getMessage(), e);
        }
    }
//get all employee details
    public List<Employee> getAllEmployees(){
        return template.query("SELECT * FROM employees", (rs,rowNum)->mapRow(rs));
    }

    //find an employee
    public Employee findById(long id){
        String sql = "SELECT * FROM get_employee_by_id(?)";
        try{
            return template.queryForObject(sql,new Object[]{id},(rs,rowNum)->mapRow(rs));
        }catch(EmptyResultDataAccessException e){
            return null;
        }
    }

    //update employee
    public int updateEmployee(Employee employee){
        String sql = "UPDATE employees SET firstName=?,lastName=?,email=?,job_title=? WHERE id=?";
        return template.update(sql,employee.getFirstName(),employee.getLastName(),employee.getEmail(),employee.getJob_title(),employee.getId());
    }

    public int deleteEmp(long id, String deletedBy) {
        String insertSql = "INSERT INTO deleted (id, firstName, lastName, email, job_title, deleted_by, deleted_at) " +
                "SELECT id, firstName, lastName, email, job_title, ?, CURRENT_TIMESTAMP " +
                "FROM employees WHERE id = ?";
        template.update(insertSql, deletedBy, id);

        String deleteSql = "DELETE FROM employees WHERE id = ?";
        return template.update(deleteSql, id);
    }


    private Employee mapRow(java.sql.ResultSet rs) throws java.sql.SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getInt("id"));
        employee.setFirstName(rs.getString("firstName"));
        employee.setLastName(rs.getString("lastName"));
        employee.setEmail(rs.getString("email"));
        employee.setJob_title(rs.getString("job_title"));
        return employee;
    }


}
