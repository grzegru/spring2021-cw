package pl.javastart.healtycalc;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api")
class BodyWeightController {
    private static final String BMI_INVALID_WEIGHT_HEIGHT =
            "invalid data, weight and height parameteres must be positive numbers";
    private static final String BMR_INVALID_WEIGHT_HEIGHT_AGE =
            "invalid data, weight, height and age parameteres must be positive numbers";
    private static final String BMR_INVALID_GENDER =
            "invalid data, gender parameter must be M for Man or W for Woman";

    private final BodyWeightCalculator bodyWeightCalculator;

    public BodyWeightController(BodyWeightCalculator bodyWeightCalculator) {
        this.bodyWeightCalculator = bodyWeightCalculator;
    }

    @GetMapping(value = "/bmi", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BMIDto> calculateBMIJson(@RequestParam double weight,
                                @RequestParam double height) {
        if (containsNonPositiveNumber(weight, height)) {
            return ResponseEntity.badRequest()
                    .header("reason", BMI_INVALID_WEIGHT_HEIGHT)
                    .build();
        }
        int bmi = bodyWeightCalculator.calculateBMI(weight, height);
        BMIDto bmiResponse = new BMIDto(weight, height, bmi);
        return ResponseEntity.ok(bmiResponse);
    }

    @GetMapping(value = "/bmi", produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> calculateBMIText(@RequestParam double weight,
                                            @RequestParam double height) {
        if (containsNonPositiveNumber(weight, height)) {
            return ResponseEntity.badRequest()
                    .header("reason", BMI_INVALID_WEIGHT_HEIGHT)
                    .build();
        }
        int bmi = bodyWeightCalculator.calculateBMI(weight, height);
        BMIDto bmiResponse = new BMIDto(weight, height, bmi);
        return ResponseEntity.ok(Integer.toString(bmiResponse.getBmi()));
    }

    @GetMapping(value = "/bmr", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<BMRDto> calculateBMRJson(@RequestParam double weight,
                        @RequestParam double height,
                        @RequestParam int age,
                        @RequestParam String gender) {
        if (containsNonPositiveNumber(weight, height, age)) {
            return ResponseEntity.badRequest()
                    .header("reason", BMR_INVALID_WEIGHT_HEIGHT_AGE)
                    .build();
        } else if (isIncorrectGenderValue(gender)) {
            return ResponseEntity.badRequest()
                    .header("reason", BMR_INVALID_GENDER)
                    .build();
        }
        int bmr = bodyWeightCalculator.calculateBMR(weight, height, age, gender);
        BMRDto bmrResponse = new BMRDto(gender, weight, height, age, bmr);
        return ResponseEntity.ok(bmrResponse);
    }

    @GetMapping(value = "/bmr", produces = MediaType.TEXT_PLAIN_VALUE)
    ResponseEntity<String> calculateBMRText(@RequestParam double weight,
                                        @RequestParam double height,
                                        @RequestParam int age,
                                        @RequestParam String gender) {
        if (containsNonPositiveNumber(weight, height, age)) {
            return ResponseEntity.badRequest()
                    .header("reason", BMR_INVALID_WEIGHT_HEIGHT_AGE)
                    .build();
        } else if (isIncorrectGenderValue(gender)) {
            return ResponseEntity.badRequest()
                    .header("reason", BMR_INVALID_GENDER)
                    .build();
        }
        int bmr = bodyWeightCalculator.calculateBMR(weight, height, age, gender);
        BMRDto bmrResponse = new BMRDto(gender, weight, height, age, bmr);
        return ResponseEntity.ok(bmrResponse.getBmr() + "kcal");
    }

    private boolean containsNonPositiveNumber(double... params) {
        for (double param : params) {
            if (param <= 0) {
                return true;
            }
        }
        return false;
    }

    private boolean isIncorrectGenderValue(String gender) {
        return !gender.equals("M") && !gender.equals("W");
    }
}
