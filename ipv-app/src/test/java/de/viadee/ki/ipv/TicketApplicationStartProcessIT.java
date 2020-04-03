package de.viadee.ki.ipv;

import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import de.viadee.ki.ipv.config.Config;
import de.viadee.ki.ipv.model.ProcessConstants;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { Config.class })
public class TicketApplicationStartProcessIT {

    @Autowired
    RestTemplate restTemplate;

    /**
     * Starte einen Ticketkauf für eine nicht überlebende Person
     * 
     * @throws Exception
     */
    @Test
    public void ticketApplicationStartProcess_BenTot_IT() throws Exception {
        JSONObject personDataVariables = createPersondataAsJSON(1, 37, "male", 12.45, "Ben Wolters", 2, 2, 3, "TXD123", "D20", "C", 1);
        sendPerson2Process(personDataVariables);
    }

    /**
     * Starte einen Ticketkauf für eine überlebende Person mit automatischer Verarbeitung
     * 
     * @throws Exception
     */
    @Test
    public void ticketApplicationStartProcess_BenSurvived_IT() throws Exception {
        JSONObject personDataVariables = createPersondataAsJSON(2, 27, "female", 52.45, "Ben Female Wolters", 1, 2, 0, "TXD123", "D20", "C", 1);
        sendPerson2Process(personDataVariables);
    }

    /**
     * Starte einen Ticketkauf für eine anormale Person
     * 
     * @throws Exception
     */
    @Test
    public void ticketApplicationStartProcess_BenAnomalie_IT() throws Exception {
        JSONObject personDataVariables = createPersondataAsJSON(3, 137, "male", 0, "Ben Anomalie", 3, 20, 30, ".", "D20", "C", 1);
        sendPerson2Process(personDataVariables);
    }

    private void sendPerson2Process(JSONObject personDataVariables) throws URISyntaxException, JSONException {
        URI url = new URI("http://localhost:8080/rest/process-definition/key/ticketbestellung/start");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject startProcessinstance = new JSONObject();
        startProcessinstance.put("variables", personDataVariables);

        HttpEntity<String> request = new HttpEntity<String>(startProcessinstance.toString(), headers);
        String response = restTemplate.postForObject(url, request, String.class);
        System.out.println("response: " + response);
    }

    private JSONObject createPersondataAsJSON(int id, int age, String sex, double fare, String name, int pclass,
            int sibsp, int parch, String ticket, String cabin, String embarked, int survived) throws JSONException {
        JSONObject idJO = new JSONObject();
        idJO.put("value", id + 10000);
        idJO.put("type", "Integer");
        JSONObject ageJO = new JSONObject();
        ageJO.put("value", age);
        ageJO.put("type", "Integer");
        JSONObject sexJO = new JSONObject();
        sexJO.put("value", sex);
        sexJO.put("type", "String");
        JSONObject fareJO = new JSONObject();
        fareJO.put("value", fare);
        fareJO.put("type", "Double");
        JSONObject nameJO = new JSONObject();
        nameJO.put("value", name);
        nameJO.put("type", "String");
        JSONObject pclassJO = new JSONObject();
        pclassJO.put("value", pclass);
        pclassJO.put("type", "Integer");
        JSONObject parchJO = new JSONObject();
        parchJO.put("value", parch);
        parchJO.put("type", "Integer");
        JSONObject sibspJO = new JSONObject();
        sibspJO.put("value", sibsp);
        sibspJO.put("type", "Integer");
        JSONObject cabinJO = new JSONObject();
        cabinJO.put("value", cabin);
        cabinJO.put("type", "String");
        JSONObject ticketJO = new JSONObject();
        ticketJO.put("value", ticket);
        ticketJO.put("type", "String");
        JSONObject embarkedJO = new JSONObject();
        embarkedJO.put("value", embarked);
        embarkedJO.put("type", "String");
        JSONObject survivedJO = new JSONObject();
        survivedJO.put("value", survived);
        survivedJO.put("type", "Integer");

        JSONObject variables = new JSONObject();
        variables.put(ProcessConstants.INT_PERSON_ID, idJO);
        variables.put(ProcessConstants.INT_PERSON_AGE, ageJO);
        variables.put(ProcessConstants.INT_PERSON_SEX, sexJO);
        variables.put(ProcessConstants.INT_PERSON_FARE, fareJO);
        variables.put(ProcessConstants.INT_PERSON_NAME, nameJO);
        variables.put(ProcessConstants.INT_PERSON_PCLASS, pclassJO);
        variables.put(ProcessConstants.INT_PERSON_SIBSP, sibspJO);
        variables.put(ProcessConstants.INT_PERSON_PARCH, parchJO);
        variables.put(ProcessConstants.INT_PERSON_TICKET, ticketJO);
        variables.put(ProcessConstants.INT_PERSON_EMBARKED, embarkedJO);
        variables.put(ProcessConstants.INT_PERSON_SURVIVED, survivedJO);

        return variables;
    }

}
