package com.example.daycare.controller;

import com.example.daycare.Dto.DairyDto;
import com.example.daycare.Repository.DairyRepository;
import com.example.daycare.Service.MapStructMapper;
import com.example.daycare.model.Child;

import com.example.daycare.model.Dairy1;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/Dairy")
public class DairyController {
    private final DairyRepository dairyRepository;
    private  final MapStructMapper mapStructImp;

    public DairyController(DairyRepository dairyRepository,MapStructMapper mapper) {
        this.dairyRepository = dairyRepository;
        this.mapStructImp=mapper;
    }
    @GetMapping("/gatAll")
    public List<Dairy1> getAll()
    {
        return dairyRepository.findAll();
    }
    @GetMapping("/gatAllDto")
    public List<DairyDto> getAllDto()
    {
        return mapStructImp.toListDairyDto(dairyRepository.findAll()) ;
    }
    @PostMapping("/addDairy")
    public Dairy1 addDairy(@RequestBody Dairy1 dairy){
        //Dairy newDairy= dairyRepository.save(dairy);
        return dairyRepository.save(dairy);
    }
    @PostMapping("/addDairyDto")
    public DairyDto addDairyDto(@RequestBody Dairy1 dairy){
        //Dairy newDairy= dairyRepository.save(dairy);
        return mapStructImp.toDairyDto(dairyRepository.save(dairy));
    }

}
