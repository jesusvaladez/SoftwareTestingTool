package org.apache.ambari.server.api.query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang.ObjectUtils;
public class JpaSortBuilder<T> {
    public JpaSortBuilder() {
    }

    public java.util.List<javax.persistence.criteria.Order> buildSortOrders(org.apache.ambari.server.controller.spi.SortRequest sortRequest, org.apache.ambari.server.api.query.JpaPredicateVisitor<T> visitor) {
        if ((null == sortRequest) || (null == sortRequest.getProperties())) {
            return java.util.Collections.emptyList();
        }
        javax.persistence.criteria.CriteriaBuilder builder = visitor.getCriteriaBuilder();
        java.util.List<org.apache.ambari.server.controller.spi.SortRequestProperty> sortProperties = sortRequest.getProperties();
        java.util.List<javax.persistence.criteria.Order> sortOrders = new java.util.ArrayList<>(sortProperties.size());
        for (org.apache.ambari.server.controller.spi.SortRequestProperty sort : sortProperties) {
            java.lang.String propertyId = sort.getPropertyId();
            java.util.List<? extends javax.persistence.metamodel.SingularAttribute<?, ?>> singularAttributes = visitor.getPredicateMapping(propertyId);
            if ((null == singularAttributes) || (singularAttributes.size() == 0)) {
                continue;
            }
            javax.persistence.criteria.Path<?> path = null;
            for (javax.persistence.metamodel.SingularAttribute<?, ?> singularAttribute : singularAttributes) {
                if (null == path) {
                    javax.persistence.criteria.CriteriaQuery<T> query = visitor.getCriteriaQuery();
                    java.util.Set<javax.persistence.criteria.Root<?>> roots = query.getRoots();
                    if ((null != roots) && (!roots.isEmpty())) {
                        java.util.Iterator<javax.persistence.criteria.Root<?>> iterator = roots.iterator();
                        while (iterator.hasNext()) {
                            javax.persistence.criteria.Root<?> root = iterator.next();
                            java.lang.Class<?> visitorEntityClass = visitor.getEntityClass();
                            if (org.apache.commons.lang.ObjectUtils.equals(visitorEntityClass, root.getJavaType()) || org.apache.commons.lang.ObjectUtils.equals(visitorEntityClass, root.getModel().getJavaType())) {
                                path = root.get(singularAttribute.getName());
                                break;
                            }
                        } 
                    }
                    if (null == path) {
                        path = query.from(visitor.getEntityClass()).get(singularAttribute.getName());
                    }
                } else {
                    path = path.get(singularAttribute.getName());
                }
            }
            javax.persistence.criteria.Order sortOrder = null;
            if (sort.getOrder() == org.apache.ambari.server.controller.spi.SortRequest.Order.ASC) {
                sortOrder = builder.asc(path);
            } else {
                sortOrder = builder.desc(path);
            }
            sortOrders.add(sortOrder);
        }
        return sortOrders;
    }
}