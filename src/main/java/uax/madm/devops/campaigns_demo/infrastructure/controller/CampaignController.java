package uax.madm.devops.campaigns_demo.infrastructure.controller;

import java.util.List;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import uax.madm.devops.campaigns_demo.application.services.CampaignService;
import uax.madm.devops.campaigns_demo.domain.model.Campaign;
import uax.madm.devops.campaigns_demo.infrastructure.dto.CampaignDto;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/campaigns")
public class CampaignController {

    private CampaignService campaignService;

    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    @GetMapping
    public List<CampaignDto> listCampaigns() {
        List<Campaign> models = campaignService.findCampaigns();
        List<CampaignDto> campaignDtos = models.stream()
                .map(CampaignDto.Mapper::toDto)
                .collect(Collectors.toList());
        return campaignDtos;
    }

    @GetMapping(value = "/{id}")
    public CampaignDto findOne(@PathVariable Long id) {
        Campaign model = campaignService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return CampaignDto.Mapper.toDto(model);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CampaignDto create(@RequestBody @Valid CampaignDto newCampaign) {
        Campaign model = CampaignDto.Mapper.toModel(newCampaign);
        Campaign createdModel = this.campaignService.save(model);
        return CampaignDto.Mapper.toDto(createdModel);
    }

    @PutMapping(value = "/{id}")
    public CampaignDto update(@PathVariable Long id, @RequestBody @Validated(CampaignDto.CampaignUpdateValidationData.class) CampaignDto updatedCampaign) {
        Campaign model = CampaignDto.Mapper.toModel(updatedCampaign);
        Campaign createdModel = this.campaignService.updateCampaign(id, model)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return CampaignDto.Mapper.toDto(createdModel);
    }
}
