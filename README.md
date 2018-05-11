# GraphQL-Retrofit-Converter
==============================

A Retrofit 2 `Converter.Factory` for GraphQL.


Usage
-----

Add a converter factory when building your `Retrofit` instance using either the
`stringBased` or `bytesBased` factory methods:
```
val retrofit = Retrofit.Builder()
    .baseUrl("https://example.com/")
    .addConverterFactory(GraphQLConverter.create(this))
    .build()
```

Now when you build your API interface use it like this,
```
@POST("/")
@GraphQuery("filename")
fun getResponse(@Body query: QueryContainerBuilder): Call<T> 
```
Note filename here refers to filename.graphql file which contains the actual GraphQL query 
that should be placed in assets->graphql-> <<filename.graphql>>.
 
QueryContainerBuilder is an in-built model class that allows to set variables
(if any) in the graphql query.Hence it might also be null 

The variables in the query can be set like this,
```
val queryContainer = QueryContainerBuilder()
queryContainer.putVariable("id",1)

```

Download
--------

Grab via Maven:
```xml
<dependency>
  <groupId>com.ramkishorevs.graphqlretrofitconverter</groupId>
  <artifactId>graphqlconverter</artifactId>
  <version>1.0.0</version>
  <type>pom</type>
</dependency>
```
or via Gradle
```groovy
compile 'com.ramkishorevs.graphqlretrofitconverter:graphqlconverter:1.0.0'
```



License
=======

    Copyright 2018 Ramkishore V S

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

