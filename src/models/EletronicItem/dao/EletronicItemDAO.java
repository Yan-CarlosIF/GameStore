package models.EletronicItem.dao;

import models.EletronicItem.entities.EletronicItem;

import java.util.List;

public interface EletronicItemDAO {
    List<EletronicItem> find();
    EletronicItem findById(String id);
    List<EletronicItem> findByProducer(String producer);
    List<EletronicItem> findByType(String type);
    void insert(EletronicItem eletronicItem);
    void update(EletronicItem eletronicItem);
    void delete(String id);
}
