package com.example.daycare.Service;

import com.example.daycare.Dto.ChildDto;
import com.example.daycare.Dto.DairyDto;
import com.example.daycare.Dto.NannyDto;
import com.example.daycare.Dto.ParentDto;
import com.example.daycare.model.Child;
import com.example.daycare.model.Dairy1;
import com.example.daycare.model.Nanny;
import com.example.daycare.model.Parent;
import org.mapstruct.Mapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Mapper(componentModel = "spring")
public  interface MapStructMapper {
     Child toChild(ChildDto childDto);
  //  ChildDto toChildDto(Child child);
     List<ChildDto> toListChildDto(List<Child> childList);
     List<Child> toListChild(List<ChildDto> childDtoList);
     Dairy1 toDairy(DairyDto dairyDto);
     DairyDto toDairyDto(Dairy1 dairy);
    List<DairyDto> toListDairyDto(List<Dairy1> dairyList);
    Nanny toNanny(NannyDto nannyDto);
     NannyDto toNannyDto(Nanny nanny);
    List<NannyDto> toListNannyDto(List<Nanny> nannyList);
    Parent toParent(ParentDto parentDto);
     ParentDto toParentDto(Parent parent);
    List<ParentDto> toListParentDto(List<Parent> parentList);
    //ChildDto toChildDto(Child child);
    default  ChildDto toChildDto(Child child) throws IOException {
        ChildDto childDto=new ChildDto();
        childDto.setIdChild(child.getIdChild());
        childDto.setName(child.getName());
        childDto.setBirthDate(child.getBirthDate());
        childDto.setArrived(child.getArrived());
        if(child.getImageChild()!=null) {
            //
            String dir = System.getProperty("user.dir") + "\\images\\";
            //מקבל מחרוזת והופך לניתוב
            Path fileUrl = Paths.get(dir + child.getImageChild());
            //  try {
            //הפיכת התמונה מהניתוב שלה למערך של ביטים
          //  byte[] arr = Files.readAllBytes(fileUrl);
          //  childDto.setImageChild(arr);
        }
//        }
//        catch (Exception e){
//
//        }
        return  childDto;
    }

}
