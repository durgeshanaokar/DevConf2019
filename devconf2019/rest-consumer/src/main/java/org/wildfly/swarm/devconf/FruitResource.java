package org.wildfly.swarm.devconf;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import org.wildfly.swarm.devconf.Fruit;

import org.aerogear.kafka.cdi.annotation.Consumer;
import org.aerogear.kafka.cdi.annotation.KafkaConfig;
import org.jboss.logging.Logger;

/**
 * @author Durgesh Anaokar
 */
@Path("/fruits")
@ApplicationScoped
//@KafkaConfig(bootstrapServers = "#{KAFKA_SERVICE_HOST}:#{KAFKA_SERVICE_PORT}")
@KafkaConfig(bootstrapServers = "localhost:9092")
public class FruitResource {
	private Logger logger = Logger.getLogger(FruitResource.class.getName());

    @PersistenceContext(unitName = "FruitCU")
    private EntityManager em;

    @GET
    @Produces("application/json")
    public Fruit[] get() {
    	logger.info("In side method get()");
    	Fruit[] temp = em
                .createNamedQuery("fruit.findAll", Fruit.class)
                .getResultList()
                .toArray(new Fruit[0]);
    	logger.info("Number of records fetched "+ temp.length);
        return temp;
    }

    @Consumer(topics = "fruit_topic", keyType = Integer.class, groupId = "fruit_processor")
    @Transactional
    public void processFruit(final Integer key, final Fruit fruitData) {
        logger.error("We received: " + fruitData);

        try {
            Fruit entity = em.find(Fruit.class, key);
            if(entity!= null)
			{
				entity.setName(fruitData.getName());
				entity.setId(fruitData.getId());
			}else{
				entity = fruitData;
			}
            logger.info("Entity about to merge through processFruit() "+entity);
            
            em.merge(entity);

            logger.info("Entity merged through processFruit() "+entity);
        } catch (Exception e) {
           logger.error("Error in merging in processFruit() "+e.getMessage());
        }
    
    }
    
   private Response error(int code, String message) {
        return Response
                .status(code)
                .entity(Json.createObjectBuilder()
                                .add("error", message)
                                .add("code", code)
                                .build()
                )
                .build();
    }
}
