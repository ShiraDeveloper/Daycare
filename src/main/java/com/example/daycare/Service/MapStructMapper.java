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
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapStructMapper {

    Child toChild(ChildDto childDto);

    ChildDto toChildDto(Child child);

    List<ChildDto> toListChildDto(List<Child> childList);

    Dairy1 toDairy(DairyDto dairyDto);

    DairyDto toDairyDto(Dairy1 dairy);

    List<DairyDto> toListDairyDto(List<Dairy1> dairyList);

    NannyDto toNannyDto(Nanny nanny);

    List<NannyDto> toListNannyDto(List<Nanny> nannyList);

    Parent toParent(ParentDto parentDto);

    ParentDto toParentDto(Parent parent);

    List<ParentDto> toListParentDto(List<Parent> parentList);
}
