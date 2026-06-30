package com.example.daycare.service;

import com.example.daycare.dto.ChildDto;
import com.example.daycare.dto.DairyDto;
import com.example.daycare.dto.NannyDto;
import com.example.daycare.dto.ParentDto;
import com.example.daycare.model.Child;
import com.example.daycare.model.Dairy;
import com.example.daycare.model.Nanny;
import com.example.daycare.model.Parent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MapStructMapper {

    Child toChild(ChildDto childDto);

    ChildDto toChildDto(Child child);

    List<ChildDto> toListChildDto(List<Child> childList);

    @Mapping(target = "child", ignore = true)
    Dairy toDairy(DairyDto dairyDto);

    @Mapping(target = "childId", source = "child.idChild")
    DairyDto toDairyDto(Dairy dairy);

    List<DairyDto> toListDairyDto(List<Dairy> dairyList);

    NannyDto toNannyDto(Nanny nanny);

    List<NannyDto> toListNannyDto(List<Nanny> nannyList);

    Parent toParent(ParentDto parentDto);

    ParentDto toParentDto(Parent parent);

    List<ParentDto> toListParentDto(List<Parent> parentList);
}
