package com.foxconn.core.pro.server.rule.engine.action.mqtt;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import com.foxconn.core.pro.server.rule.engine.action.config.mqtt.MqttProperties;
import com.foxconn.core.pro.server.rule.engine.action.constant.CommonConstant;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MqttOutboundConfiguration
{
	@Autowired
	private MqttProperties mqttProperties;

	@Bean
	public MqttPahoClientFactory mqttClientFactory()
	{
		DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
		factory.setUserName(mqttProperties.getOutbound().getUsername());
		factory.setPassword(mqttProperties.getOutbound().getPassword());
		String[] serverURIs = mqttProperties.getOutbound().getUrl().split(CommonConstant.COMMA);
		factory.setServerURIs(serverURIs);
		factory.setKeepAliveInterval(mqttProperties.getOutbound().getKeepLiveInterval());
		return factory;
	}

	@Bean
	@ServiceActivator(inputChannel = CommonConstant.MQTT_OUT_BOUND_CHANNEL)
	public MessageHandler mqttOutbound()
	{
		MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler(mqttProperties.getOutbound().getClientId(),
				mqttClientFactory());
		messageHandler.setAsync(true);
		messageHandler.setDefaultQos(mqttProperties.getOutbound().getQos());
		//messageHandler.setDefaultTopic(mqttProperties.getOutbound().getTopic());
		return messageHandler;
	}

	@Bean
	public MessageChannel mqttOutboundChannel()
	{
		return new DirectChannel();
	}

	public MqttConnectOptions getMqttConnectOptions()
	{
		MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
		mqttConnectOptions.setUserName(mqttProperties.getOutbound().getUsername());
		mqttConnectOptions.setPassword(mqttProperties.getOutbound().getPassword().toCharArray());
		String[] serverURIs = mqttProperties.getOutbound().getUrl().split(CommonConstant.COMMA);
		mqttConnectOptions.setServerURIs(serverURIs);
		mqttConnectOptions.setKeepAliveInterval(10);
		SSLSocketFactory factory;
		try
		{
			factory = this.createSSLSocket();
			mqttConnectOptions.setSocketFactory(factory);
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return mqttConnectOptions;
	}

	private SSLSocketFactory createSSLSocket()
	{
		//
		SSLSocketFactory sslSocketFactory;
		try
		{
			ClassLoader classLoader = MqttOutboundConfiguration.class.getClassLoader();
			InputStream caFile = classLoader.getResourceAsStream(CommonConstant.MQTT_CRT_CA_CRT);

			InputStream pcks12File = classLoader.getResourceAsStream(CommonConstant.MQTT_CRT_CLIENT_PFX);
			CertificateFactory certificateFactory = CertificateFactory.getInstance(CommonConstant.X_509);
			Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(caFile);
			if (certificates.isEmpty())
			{
				throw new IllegalArgumentException(CommonConstant.EXPECTED_NON_EMPTY);
			}
			// Put the certificates author to key store.
			char[] password = "".toCharArray(); // Any password will work.
			KeyStore caKeyStore = newEmptyKeyStore(password);
			int index = 0;
			for (Certificate certificate : certificates)
			{
				String certificateAlias = Integer.toString(index++);
				caKeyStore.setCertificateEntry(certificateAlias, certificate);
			}

			// Create the certificates of client
			KeyStore clientkeyStore = KeyStore.getInstance(CommonConstant.PKCS12);
			clientkeyStore.load(pcks12File, password);
			caFile.close();
			pcks12File.close();
			// Building SSL context for future use
			KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(CommonConstant.SUNX_509);
			keyManagerFactory.init(clientkeyStore, password);
			KeyManager[] keyManagers = keyManagerFactory.getKeyManagers();
			TrustManagerFactory trustManagerFactory = TrustManagerFactory
					.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(caKeyStore);
			TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
			if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager))
			{
				throw new IllegalStateException(CommonConstant.UNEXPECTED_DEFAULT + Arrays.toString(trustManagers));
			}
			//
			SSLContext sslContext = SSLContext.getInstance(CommonConstant.TLSV1);
			sslContext.init(keyManagers, trustManagers, null);
			sslSocketFactory = sslContext.getSocketFactory();
		} catch (GeneralSecurityException e)
		{
			throw new RuntimeException(e);
		} catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		return sslSocketFactory;
	}

	private static KeyStore newEmptyKeyStore(char[] password) throws GeneralSecurityException
	{
		try
		{
			KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
			keyStore.load(null, password); // 'null' creates an empty key store.
			return keyStore;
		} catch (IOException e)
		{
			throw new AssertionError(e);
		}
	}
}
