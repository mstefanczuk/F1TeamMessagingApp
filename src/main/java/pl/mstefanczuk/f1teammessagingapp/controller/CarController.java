package pl.mstefanczuk.f1teammessagingapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.mstefanczuk.f1teammessagingapp.model.CarInfo;
import pl.mstefanczuk.f1teammessagingapp.service.CarService;

@Controller
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @RequestMapping
    public String index(CarInfo carInfo) {
        return "index";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String submit(CarInfo carInfo) {
        carService.updateCarInfo(carInfo);
        return "index";
    }

    @RequestMapping(value = "pitstop-request", method = RequestMethod.GET)
    public String requestForPitstop() {
        carService.requestForPitstop();

        return "redirect:";
    }
}
