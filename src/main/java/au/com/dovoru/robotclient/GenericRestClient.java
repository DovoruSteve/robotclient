package au.com.dovoru.robotclient;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Transmit requests and return responses to a REST API
 * @author stephen
 *
 */
@Component
public class GenericRestClient {
	public <K, T> K post(String url, T request, Class<K> clazz) throws WebClientResponseException {
		K response =  WebClient.builder().build().post().uri(url).body(BodyInserters.fromValue(request)).retrieve().bodyToMono(clazz).block();
		return response;
	}	
	public <K, T> K put(String url, T request, Class<K> clazz) throws WebClientResponseException {
		RequestBodySpec spec = WebClient.builder().build().put().uri(url);
		K response;
		if (request != null) {
			response = spec.body(BodyInserters.fromValue(request)).retrieve().bodyToMono(clazz).block();
		} else {
			response = spec.retrieve().bodyToMono(clazz).block();
		}
		return response;
	}
	public <K, T> K get(String url, Class<K> clazz) throws WebClientResponseException {
		K response =  WebClient.builder().build().get().uri(url).retrieve().bodyToMono(clazz).block();
		return response;
	}
}
