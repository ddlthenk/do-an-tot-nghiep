package com.datn.commonbase.repository;

import co.elastic.clients.elasticsearch._types.Refresh;
import co.elastic.clients.elasticsearch.core.*;
import com.datn.commonbase.client.SearchClient;
import com.datn.commonbase.dto.SearchProductDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public class DocumentRepositoryImpl {
    Refresh REFRESH = Refresh.True;
    SearchClient searchClient = SearchClient.getInstance();
    private static DocumentRepositoryImpl _instance;

    public static DocumentRepositoryImpl getInstance() {
        if (_instance == null) {
            _instance = new DocumentRepositoryImpl();
        }
        return _instance;
    }

    private static final Logger _log = LogManager.getLogger(DocumentRepositoryImpl.class);

    public IndexResponse index(SearchProductDto searchProductDto) throws IOException {
        return searchClient.executeWithClient(client ->
                client.index(i ->
                        i.index("products").id(String.valueOf(searchProductDto.getProductId())).document(searchProductDto).refresh(REFRESH)));
    }


    public BulkResponse indexBulk(List<SearchProductDto> productList) throws IOException {
        return searchClient.executeWithClient(client -> {
            BulkRequest.Builder br = new BulkRequest.Builder();
            try {
                for (SearchProductDto searchProductDto : productList) {
                    br.operations(op -> op.index(idx -> idx.index("products").id(String.valueOf(searchProductDto.getProductId()))
                            .document(searchProductDto))).refresh(REFRESH);
                }
            } catch (Exception e) {
                _log.error(productList.toString());
            }
            return client.bulk(br.build());
        });
    }

    public GetResponse<? extends SearchProductDto> getById(String index, String id, Class<? extends SearchProductDto> clazz) throws IOException {
        return searchClient.executeWithClient(client -> client.get(g -> g.index(index).id(id), clazz));
    }

    public SearchResponse<? extends SearchProductDto> search(Class<? extends SearchProductDto> clazz, SearchRequest searchRequest) throws IOException {
        return searchClient.executeWithClient(client -> client.search(searchRequest, clazz));
    }

    public DeleteResponse delete(String index, String id) throws IOException {
        return searchClient.executeWithClient(client ->
                client.delete(d -> d.index(index).id(id).refresh(REFRESH)));
    }

    public BulkResponse delete(String index, List<String> ids) throws IOException {
        return searchClient.executeWithClient(client -> {
            BulkRequest.Builder br = new BulkRequest.Builder();
            for (String id : ids) {
                br.operations(op -> op.delete(d -> d.index(index).id(id))).refresh(REFRESH);
            }
            return client.bulk(br.build());
        });
    }

    public MgetResponse<? extends SearchProductDto> getByIds(String index, List<String> ids, Class<? extends SearchProductDto> clazz) throws IOException {
        return searchClient.executeWithClient(client -> client.mget(builder -> builder.index(index).ids(ids), clazz));
    }

    public CountResponse count(CountRequest countRequest) throws IOException {
        return searchClient.executeWithClient(client -> client.count(countRequest));
    }

    public DeleteByQueryResponse deleteAll(String index) throws IOException {
        return searchClient.executeWithClient(client -> {
            DeleteByQueryRequest delete = DeleteByQueryRequest.of(builder -> builder.index(index).refresh(true).query(q -> q.matchAll(m -> m)));
            return client.deleteByQuery(delete);
        });
    }
}
