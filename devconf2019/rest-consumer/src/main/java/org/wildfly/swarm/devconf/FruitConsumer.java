package org.wildfly.swarm.devconf;

import org.aerogear.kafka.cdi.annotation.Consumer;
import org.aerogear.kafka.cdi.annotation.KafkaConfig;
import org.jboss.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 * @author Durgesh Anaokar
 */
//@KafkaConfig(bootstrapServers = "#{KAFKA_SERVICE_HOST}:#{KAFKA_SERVICE_PORT}")
@KafkaConfig(bootstrapServers = "localhost:9092")
public class FruitConsumer {
    private Logger logger = Logger.getLogger(FruitConsumer.class.getName());
    
    @PersistenceContext(unitName = "FruitCU")
    private EntityManager em;


    @Consumer(topics = "fruit_topic", keyType = Integer.class, groupId = "fruit_processor")
    @Transactional
    public void processFruit(final Integer key, final Fruit fruitData) {
        logger.error("We received: " + fruitData);

        try {
            Fruit entity = em.find(Fruit.class, key);

            entity.setName(fruitData.getName());
            em.merge(entity);

            logger.info("Entity merged through processFruit() "+entity);
        } catch (Exception e) {
           logger.error("Error in merging in processFruit() "+e.getMessage());
        }
    
    }
}
