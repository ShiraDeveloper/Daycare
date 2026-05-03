package com.example.daycare.controller;


import com.example.daycare.Dto.ChildDto;
import com.example.daycare.Repository.ChildRepository;
import com.example.daycare.Service.MapStructMapper;
import com.example.daycare.model.Child;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("api/Child")
public class ChildController {
   private final ChildRepository childRepository;
    private  String directory=System.getProperty("user.dir")+"\\images\\";
    private  final MapStructMapper mapStructImp;
    @Autowired
    public ChildController(ChildRepository childRepository,MapStructMapper mapStructImp) {

        this.childRepository = childRepository;
        this.mapStructImp=mapStructImp;
    }
@GetMapping("/getAll")
    public List<Child> getAll()
    {
        return childRepository.findAll();
    }
    @GetMapping("/getAllDto")
    public List<ChildDto> getAllDto()
    {
        return mapStructImp.toListChildDto(childRepository.findAll());
    }
    @PostMapping("/addChild")
    public Child addChild(@RequestBody Child child){
        Child newChild= childRepository.save(child);
        return  newChild;
    }

//    @PostMapping("/addChildDto")
//    public ChildDto addChildDto(@RequestBody Child child){
//        ChildDto newChild= mapStructImp.toChildDto(childRepository.save(child));
//        return  newChild;
//    }
@PostMapping("/addChildDto")
public ResponseEntity<ChildDto> addChildDto(@RequestPart("file") MultipartFile file,
                                                @RequestPart("child") ChildDto childDto) throws IOException {
    //לשמור את התמונה בתקיית תמונות
    String dir=directory+file.getOriginalFilename();
    Path path= Paths.get(dir);

    Files.write(path,file.getBytes());
    Child child=mapStructImp.toChild(childDto);
    child.setImageChild(file.getOriginalFilename());
    ChildDto childDto1= mapStructImp.toChildDto(childRepository.save(child));
    return new ResponseEntity(childDto1, HttpStatus.CREATED);
}

}
