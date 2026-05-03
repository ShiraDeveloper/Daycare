package com.example.daycare.controller;

import com.example.daycare.Dto.NannyDto;
import com.example.daycare.Repository.ChildRepository;
import com.example.daycare.Repository.NannyRepository;
import com.example.daycare.Service.MapStructMapper;
import com.example.daycare.model.Child;
import com.example.daycare.model.Nanny;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/Nanny")
public class NannyController {
    private final NannyRepository nannyRepository;
    private  final MapStructMapper mapStructImp;

    public NannyController(NannyRepository nannyRepository,MapStructMapper mapper) {
        this.nannyRepository = nannyRepository;
        this.mapStructImp=mapper;
    }

    @GetMapping("/getAll")
    public List<Nanny> getAll()
    {
        return nannyRepository.findAll();
    }
    @GetMapping("/getAllDto")
    public List<NannyDto> getAllDto()
    {
        return mapStructImp.toListNannyDto(nannyRepository.findAll());
    }
    @PostMapping("/addNanny")
    public Nanny addNanny(@RequestBody Nanny nanny){
        //Child newChild= nannyRepository.save(nanny);
        return  nannyRepository.save(nanny);
    }
    @PostMapping("/addNannyDto")
    public NannyDto addNannyDto(@RequestBody Nanny nanny){
        //Child newChild= nannyRepository.save(nanny);
        return mapStructImp.toNannyDto(nannyRepository.save(nanny));
    }

}
