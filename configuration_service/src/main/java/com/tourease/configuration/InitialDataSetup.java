package com.tourease.configuration;

import com.tourease.configuration.models.entities.Configuration;
import com.tourease.configuration.models.entities.Country;
import com.tourease.configuration.models.enums.Field;
import com.tourease.configuration.repositories.ConfigurationRepository;
import com.tourease.configuration.repositories.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitialDataSetup implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final ConfigurationRepository configurationRepository;

    @Override
    public void run(String... args) {
        initData();
    }

    private void initData() {
        if (countryRepository.count() == 0) {
            String[] countries = {
                    "China", "India", "United States", "Indonesia", "Pakistan", "Brazil",
                    "Nigeria", "Bangladesh", "Russia", "Mexico", "Japan", "Ethiopia",
                    "Philippines", "Egypt", "Vietnam", "DR Congo", "Turkey", "Iran",
                    "Germany", "Thailand", "United Kingdom", "France", "Italy", "Tanzania",
                    "South Africa", "Myanmar", "Kenya", "South Korea", "Colombia",
                    "Spain", "Ukraine", "Argentina", "Algeria", "Sudan", "Uganda",
                    "Iraq", "Poland", "Canada", "Morocco", "Saudi Arabia", "Uzbekistan",
                    "Malaysia", "Afghanistan", "Venezuela", "Peru", "Angola", "Ghana",
                    "Mozambique", "Yemen", "Nepal", "Madagascar", "Cameroon", "Côte d'Ivoire",
                    "North Korea", "Australia", "Niger", "Taiwan", "Sri Lanka", "Burkina Faso",
                    "Mali", "Romania", "Malawi", "Chile", "Kazakhstan", "Zambia",
                    "Guatemala", "Ecuador", "Syria", "Netherlands", "Senegal",
                    "Cambodia", "Chad", "Somalia", "Zimbabwe", "Guinea", "Rwanda",
                    "Benin", "Tunisia", "Burundi", "Bolivia", "Belgium", "Cuba",
                    "Haiti", "South Sudan", "Dominican Republic", "Czech Republic",
                    "Greece", "Jordan", "Portugal", "Azerbaijan", "Sweden", "Honduras",
                    "United Arab Emirates", "Hungary", "Belarus", "Tajikistan", "Austria",
                    "Papua New Guinea", "Serbia", "Switzerland", "Israel", "Togo",
                    "Sierra Leone", "Hong Kong", "Laos", "Paraguay", "Bulgaria",
                    "Libya", "Lebanon", "Kyrgyzstan", "Nicaragua", "El Salvador",
                    "Turkmenistan", "Singapore", "Denmark", "Finland", "Slovakia",
                    "Norway", "Oman", "State of Palestine", "Costa Rica", "Liberia",
                    "Ireland", "New Zealand", "Central African Republic", "Mauritania",
                    "Panama", "Kuwait", "Croatia", "Moldova", "Georgia", "Eritrea",
                    "Uruguay", "Bosnia and Herzegovina", "Mongolia", "Armenia",
                    "Jamaica", "Qatar", "Albania", "Puerto Rico", "Lithuania",
                    "Namibia", "Gambia", "Botswana", "Gabon", "Lesotho", "North Macedonia",
                    "Slovenia", "Guinea-Bissau", "Latvia", "Bahrain", "Equatorial Guinea",
                    "Timor-Leste", "Estonia", "Mauritius", "Cyprus", "Eswatini",
                    "Djibouti", "Fiji", "Réunion", "Comoros", "Guyana", "Bhutan",
                    "Solomon Islands", "Macao", "Montenegro", "Luxembourg", "Western Sahara",
                    "Suriname", "Cabo Verde", "Maldives", "Guadeloupe", "Brunei",
                    "Malta", "Bahamas", "Martinique", "Belize", "Iceland", "French Guiana",
                    "Vanuatu", "Barbados", "New Caledonia", "French Polynesia",
                    "Mayotte", "Sao Tome & Principe", "Samoa", "Saint Lucia",
                    "Guam", "Channel Islands", "Curacao", "Kiribati", "St. Vincent & Grenadines",
                    "Grenada", "Tonga", "Micronesia", "Aruba", "Saint Kitts & Nevis",
                    "Seychelles", "Antigua & Barbuda", "Andorra", "Dominica",
                    "Marshall Islands", "Saint Martin", "Monaco", "Liechtenstein",
                    "San Marino", "Palau", "Tuvalu", "Nauru", "Saint Barthelemy",
                    "Saint Pierre & Miquelon", "Montserrat", "Falkland Islands",
                    "Christmas Island", "Norfolk Island", "Niue", "Tokelau",
                    "Vatican City"
            };

            countryRepository.saveAll(Arrays.stream(countries).map(Country::new).toList());

        }

        if (configurationRepository.count() == 0) {
            Configuration emailFrom = new Configuration(Field.EMAIL_FROM, "tekanpicha@gmail.com");
            Configuration emailPassword = new Configuration(Field.EMAIL_PASSWORD, "wlmkshrmalmltbxc");

            configurationRepository.saveAll(List.of(emailFrom, emailPassword));
        }
    }
}
