/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.Alumnos.ControlEscolare.Control;

import com.Alumnos.ControlEscolare.ML.Alumno;
import com.Alumnos.ControlEscolare.ML.AlumnoMaterias;
import com.Alumnos.ControlEscolare.ML.Materia;
import com.Alumnos.ControlEscolare.ML.Result;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Alien 9
 */
@Controller
@RequestMapping("/control")
public class AlumnoMateriasController {
    
    @GetMapping("/alumno")
    public String GetallAlumno(Model model, HttpSession session) {

        Result result = new Result();

        RestTemplate restTemplate = new RestTemplate();

        Alumno alumnoActual = (Alumno) session.getAttribute("Alumno");
        if (alumnoActual == null) {
            return "redirect:/control/login";
        }
        
        int idAlumnoActual = alumnoActual.getIdAlumno();
        
        ResponseEntity<Result<List<Alumno>>> alumnorespoce = restTemplate.exchange(
                "http://localhost:8081/ApiRest/Alumno",
                HttpMethod.GET, null, new ParameterizedTypeReference<Result<List<Alumno>>>() {});
        
         List<Alumno> alumnosFiltrados = new ArrayList<>();
    
    // Lógica de filtrado
   for (Alumno alumnoMaterias : alumnorespoce.getBody().object) {
        // Verificar si el alumno actual tiene idAlumno == 1, en cuyo caso no podrá ver su propia información
        if (idAlumnoActual == 1) {
            // El alumno con idAlumno == 1 no debe ver su propia información, 
            // por lo que la omitimos en la lista final
            if (alumnoMaterias.getIdAlumno() != 1) {
                alumnosFiltrados.add(alumnoMaterias);
            }
        } else {
            // El alumno normal puede ver su propia información, además de otros alumnos si corresponde
            if (alumnoMaterias.getIdAlumno() == idAlumnoActual || idAlumnoActual == 1) {
                alumnosFiltrados.add(alumnoMaterias);
            }
        }
    }

        
//        model.addAttribute("alumno", alumnorespoce.getBody().object);
        model.addAttribute("alumno", alumnosFiltrados);
         model.addAttribute("isAdmin", idAlumnoActual == 1);
        
        return "AlumnoIndex";
    }
    //pagina donde se visualiza loos alumnos
    
    
    @GetMapping("/AlumnoMateriaId/{IdAlumno}")
    public String GetallAlumnoMateria (@PathVariable int IdAlumno,Model model, HttpSession session) {

        Alumno alumnoActual = (Alumno) session.getAttribute("Alumno");
        if (alumnoActual == null) {
            return "redirect:/control/login";
        } 
        
        Result result = new Result();

        RestTemplate restTemplate = new RestTemplate();
        
         ResponseEntity<Result<List<AlumnoMaterias>>> alumnoMateriaresponce = restTemplate.exchange(
                "http://localhost:8081/ApiRest/Suma/" + IdAlumno, 
                HttpMethod.GET, null, new ParameterizedTypeReference<Result<List<AlumnoMaterias>>>() {});
             
         model.addAttribute("SumMateria", alumnoMateriaresponce.getBody().object);

        
        ResponseEntity<Result<List<AlumnoMaterias>>> alumnorespoce = restTemplate.exchange(
                "http://localhost:8081/ApiRest/AlumnoMateriaId/" + IdAlumno,
                HttpMethod.GET, null, new ParameterizedTypeReference<Result<List<AlumnoMaterias>>>() {});
                
            model.addAttribute("alumnoMateria", alumnorespoce.getBody().object);
        
        return "AlumMateTable";
    }

    
    @GetMapping("/AlumnoMateriaForm/{IdAlumno}")
    public String FormallAlumnoMateria (@PathVariable int IdAlumno,Model model, HttpSession session) {

      Alumno alumnoActual = (Alumno) session.getAttribute("Alumno");
        if (alumnoActual == null) {
            return "redirect:/control/login";
        } 
        
        Result result = new Result();

        RestTemplate restTemplate = new RestTemplate();
        
         ResponseEntity<Result<List<AlumnoMaterias>>> materiarespoce = restTemplate.exchange(
        "http://localhost:8081/ApiRest/AlmnoMateriaList/" + IdAlumno,
        HttpMethod.GET,null, 
        new ParameterizedTypeReference<Result<List<AlumnoMaterias>>>() {});
         
        
        ResponseEntity<Result<Alumno>> getByresponse = restTemplate.exchange(
                    "http://localhost:8081/ApiRest/id/" + IdAlumno,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<Result<Alumno>>() {});
        
        ResponseEntity<Result<List<AlumnoMaterias>>> alumnoMateriaresponce = restTemplate.exchange(
                "http://localhost:8081/ApiRest/Suma/" + IdAlumno, 
                HttpMethod.GET, null, new ParameterizedTypeReference<Result<List<AlumnoMaterias>>>() {});
             
         model.addAttribute("SumMateria", alumnoMateriaresponce.getBody().object);

          model.addAttribute("alumno", getByresponse.getBody().object);
          
          model.addAttribute("materia", materiarespoce.getBody().object);
        
        return "AlumMateForm";
    }
    
    @PostMapping("/AddMateriaAlu")
    @ResponseBody
    public Result AddMateriaAlumno (@RequestParam int IdAlumno, @RequestParam int IdMateria){
        
        Result result = new Result();
        
        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<Result> addresponse = restTemplate.exchange(
            "http://localhost:8081/ApiRest/NuevaMateria?idAlumno=" + IdAlumno + "&idMateria=" + IdMateria,
            HttpMethod.POST,null,Result.class);
        
         ResponseEntity<Result<List<AlumnoMaterias>>> alumnoMateriaresponce = restTemplate.exchange(
                "http://localhost:8081/ApiRest/Suma/" + IdAlumno,
                HttpMethod.GET, null, new ParameterizedTypeReference<Result<List<AlumnoMaterias>>>() {
        });

        List<AlumnoMaterias> alumnoMateriasList = alumnoMateriaresponce.getBody().object;

        double totalCosto = 0.0;
        if (alumnoMateriaresponce.getBody().object != null && !alumnoMateriaresponce.getBody().object.isEmpty()) {
            totalCosto = alumnoMateriaresponce.getBody().object.get(0).getTotalCosto(); 
        }
        
        result = addresponse.getBody();
        result.object = alumnoMateriasList;
        result.setTotalCosto(totalCosto);
        
        return result; 
    }
    
    @GetMapping("/materias")
    public String GetallMateria(Model model, HttpSession session) {

        Alumno alumnoActual = (Alumno) session.getAttribute("Alumno");
    if (alumnoActual == null) {
        return "redirect:/control/login";
    }
    
    // Obtener el id del alumno actual
    int idAlumnoActual = alumnoActual.getIdAlumno();
        
        Result result = new Result();

        RestTemplate restTemplate = new RestTemplate();
        
        ResponseEntity<Result<List<Materia>>> materiarespoce = restTemplate.exchange(
                "http://localhost:8081/ApiRest/Materia",
                HttpMethod.GET, null, new ParameterizedTypeReference<Result<List<Materia>>>() {});
        
        model.addAttribute("materia", materiarespoce.getBody().object);
         model.addAttribute("isAdmin", idAlumnoActual == 1);
        
        return "MateriaIndex";
    }
    
    @GetMapping("/edit/{IdAlumno}")
    public String AddAlumno(@PathVariable int IdAlumno, Model model, HttpSession session) {
        
        Alumno alumnoActual = (Alumno) session.getAttribute("Alumno");
        if (alumnoActual == null) {
            return "redirect:/control/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        if (IdAlumno == 0) {
            Alumno alumno = new Alumno();
            model.addAttribute("alumno", alumno);
        } else {
            ResponseEntity<Result<Alumno>> getByresponse = restTemplate.exchange(
                    "http://localhost:8081/ApiRest/id/" + IdAlumno,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<Result<Alumno>>() {
            });

            model.addAttribute("alumno", getByresponse.getBody().object);
        }

        return "AlumnoAdd";
    }
    
    @GetMapping("/editMateria/{IdMateria}")
    public String AddMateria (@PathVariable int IdMateria, Model model, HttpSession session) {
        
        Alumno alumnoActual = (Alumno) session.getAttribute("Alumno");
        if (alumnoActual == null) {
            return "redirect:/control/login";
        }

        RestTemplate restTemplate = new RestTemplate();

        if (IdMateria == 0) {
            Materia materia = new Materia();
            model.addAttribute("materia", materia);
        } else {
            ResponseEntity<Result<Materia>> getByresponse = restTemplate.exchange(
                    "http://localhost:8081/ApiRest/materiaid/" + IdMateria,
                    HttpMethod.GET, null,
                    new ParameterizedTypeReference<Result<Materia>>() {
            });

            model.addAttribute("materia", getByresponse.getBody().object);
        }

        return "MateriaForm";
    }
    
    @PostMapping("/edit")
    public String AddUpdateAlumno (@ModelAttribute("alumno") Alumno alumno ,Model model){
        
        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();
        
        if (alumno.getIdAlumno()== 0) {
            ResponseEntity<Result<List<Alumno>>> addresponse = restTemplate.exchange(
                    "http://localhost:8081/ApiRest/Alumno",
                    HttpMethod.POST,new HttpEntity<>(alumno),
                    new ParameterizedTypeReference<Result<List<Alumno>>>() {});
            
            result = addresponse.getBody();
        } else {
             ResponseEntity<Result<List<Alumno>>> updateResponce = restTemplate.exchange(
                    "http://localhost:8081/ApiRest/Alumno",
                    HttpMethod.POST,
                    new HttpEntity<>(alumno),
                    new ParameterizedTypeReference<Result<List<Alumno>>>() {});
            
            result = updateResponce.getBody();
            
        }
        return "redirect:/control/alumno";
    }
    
    @PostMapping("/editMaterias")
    public String AddUpdateMateria(@ModelAttribute("materia") Materia materia, Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        if (materia.getIdMateria() == 0) {

            ResponseEntity<Result<Materia>> response = restTemplate.exchange(
                    "http://localhost:8081/ApiRest/Materia",
                    HttpMethod.POST,
                    new HttpEntity<>(materia),
                    new ParameterizedTypeReference<Result<Materia>>() {
            });

            result = response.getBody();
        } else {
            ResponseEntity<Result<Materia>> updateresponse = restTemplate.exchange(
                    "http://localhost:8081/ApiRest/Materia",
                    HttpMethod.POST,new HttpEntity<>(materia),
                    new ParameterizedTypeReference<Result<Materia>>() {
            });

            result = updateresponse.getBody();

        }
        return "redirect:/control/materias";
    }
    
    @PostMapping("/deleteMateria/{IdMateria}")
    public String Delate(@PathVariable int IdMateria, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result> deleteresponse = restTemplate.exchange(
                "http://localhost:8081/ApiRest/DeleteMateria/" + IdMateria,
                HttpMethod.POST,null,Result.class);

        Result result = deleteresponse.getBody();

        return "redirect:/control/materias";
    }
    
    @PostMapping("/delete/{IdAlumno}")
    public String DelateAlumno(@PathVariable int IdAlumno, Model model) {

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Result> deleteresponse = restTemplate.exchange(
                "http://localhost:8081/ApiRest/Delate/" + IdAlumno,
                HttpMethod.POST,null,Result.class);

        Result result = deleteresponse.getBody();

        return "redirect:/control/alumno";
    }
    
    @GetMapping("/deleteMateria/{IdAlumnoMaterias}")
    @ResponseBody
    public Result DelateMaterias(@PathVariable int IdAlumnoMaterias, @RequestParam int IdAlumno, Model model) {

        RestTemplate restTemplate = new RestTemplate();
        Result result = new Result();

        ResponseEntity<Result> deleteresponse = restTemplate.exchange(
                "http://localhost:8081/ApiRest/DelateAlumnoMateria/" + IdAlumnoMaterias,
                HttpMethod.GET, null, new ParameterizedTypeReference<Result>() {
        });

        ResponseEntity<Result<List<AlumnoMaterias>>> alumnoMateriaresponce = restTemplate.exchange(
                "http://localhost:8081/ApiRest/Suma/" + IdAlumno,
                HttpMethod.GET, null, new ParameterizedTypeReference<Result<List<AlumnoMaterias>>>() {
        });

        List<AlumnoMaterias> alumnoMateriasList = alumnoMateriaresponce.getBody().object;

        double totalCosto = 0.0;
        if (alumnoMateriaresponce.getBody().object != null && !alumnoMateriaresponce.getBody().object.isEmpty()) {
            totalCosto = alumnoMateriaresponce.getBody().object.get(0).getTotalCosto();  // Suponiendo que el totalCosto se devuelve aquí
        }

        result = deleteresponse.getBody();
        result.object = alumnoMateriasList;
        result.setTotalCosto(totalCosto);

        return result;
    }
    
    @GetMapping("/login")
    public String Logins (Model model, HttpSession session){
        
        if (session.getAttribute("alumno") == null) {
            Alumno alumno = new Alumno();
            model.addAttribute("alumno", alumno);
        return "Login";
        } else {
          return "redirect:/control/alumno";
        }    
   }
    
    @PostMapping("/login")
    public String Login(@ModelAttribute Alumno alumno, Model model, HttpSession session,
            @RequestParam String nombre, @RequestParam String apellidopaterno) {
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Alumno> entity = new HttpEntity<>(alumno, headers);
        
        
        try {
             ResponseEntity<Result<Alumno>> loginresponse = restTemplate.exchange(
                "http://localhost:8081/ApiRest/login?nombre=" + nombre + "&apellidopaterno=" + apellidopaterno, 
                HttpMethod.POST,entity,new ParameterizedTypeReference<Result<Alumno>>(){});
             
             if (loginresponse.getStatusCodeValue() == 200) {
                System.out.println("ok");
                session.setAttribute("Alumno", loginresponse.getBody().object);
                return "redirect:/control/alumno";
            }
            
        } catch (Exception ex) {
            System.out.println( ex.getMessage());
        }
            return "Login";
   }
    
    @PostMapping("/logout")
    public String logout( Model model, HttpSession session) {
        
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Result> logoutresponse = restTemplate.exchange(
                    "http://localhost:8081/ApiRest/logout",
                    HttpMethod.POST, entity, Result.class);

            if (logoutresponse.getStatusCodeValue() == 200) {
                session.invalidate();
                return "redirect:/control/login";
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            model.addAttribute("errorMessage", "Error al cerrar sesión");
        }
        return "redirect:/control/login";
    }
    
}
