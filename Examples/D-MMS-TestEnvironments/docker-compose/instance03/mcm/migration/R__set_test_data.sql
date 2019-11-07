-- delete test data for geocasting
delete from MCM_ENTITY
where MRN in ('urn:mrn:kr:vessel:neonexsoft:sc3', 'urn:mrn:smart-navi:device:geo-server3');

-- insert test data for geocasting
-- sc는 1, sp 는 2
-- geocasting test 에서는 서비스 서버는 보내는 일만 한다.  10.0.75.1(DockerNAT) 아이피 사용안함
insert into MCM_ENTITY(MRN,IP,PORT,TYPE)
select 'urn:mrn:kr:vessel:neonexsoft:sc3',null,null,'1'
union all
select 'urn:mrn:smart-navi:device:geo-server3','10.0.75.1','9092','2';



