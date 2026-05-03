package com.example.daycare.controller;

import com.example.daycare.Dto.ParentDto;
import com.example.daycare.Repository.ParentRepository;
import com.example.daycare.Service.MapStructMapper;
import com.example.daycare.model.Child;
import com.example.daycare.model.Parent;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/Parent")
public class ParentController {
    private final ParentRepository parentRepository;
    private  final MapStructMapper mapStructImp;

    public ParentController(ParentRepository parentRepository,MapStructMapper mapper) {

        this.parentRepository = parentRepository;
        this.mapStructImp=mapper;
    }
    @GetMapping("/getAll")
    public List<Parent> getAll(){
        return parentRepository.findAll();
    }
    @GetMapping("/getAllDto")
    public List<ParentDto> getAllDto(){
        return mapStructImp.toListParentDto(parentRepository.findAll());
    }
    @PostMapping("/addParent")
    public Parent addParent(@RequestBody Parent parent){
        //Child newChild= childRepository.save(child);
        return  parentRepository.save(parent);
    }
    @PostMapping("/addParentDto")
    public ParentDto addParentDto(@RequestBody Parent parent){
        //Child newChild= childRepository.save(child);
        return mapStructImp.toParentDto(parentRepository.save(parent));
    }
}
