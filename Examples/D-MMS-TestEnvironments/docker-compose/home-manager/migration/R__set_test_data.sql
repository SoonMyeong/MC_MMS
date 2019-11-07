-- delete test data for geocasting
delete from ENTITY_HOME_MMS
where MRN_MMS in ('urn:mrn:smart-navi:device:mms1','urn:mrn:smart-navi:device:mms2','urn:mrn:smart-navi:device:mms3');
delete from MMS
where MRN in ('urn:mrn:smart-navi:device:mms1','urn:mrn:smart-navi:device:mms2','urn:mrn:smart-navi:device:mms3');


-- insert test data for geocasting
insert into MMS(MRN, URL, CREATE_DATE, UPDATE_DATE)
select 'urn:mrn:smart-navi:device:mms1', 'http://mcp-mms1_mms:8088' ,now(),now()
union all
select 'urn:mrn:smart-navi:device:mms2', 'http://mcp-mms2_mms:8088',now(),now()
union all
select 'urn:mrn:smart-navi:device:mms3',  'http://mcp-mms3_mms:8088',now(),now();

insert into ENTITY_HOME_MMS(MRN, MRN_MMS, TYPE, CREATE_DATE, UPDATE_DATE)
select 'urn:mrn:kr:vessel:neonexsoft:sc1', 'urn:mrn:smart-navi:device:mms1', 'SC'  ,now(),now()
union all
select 'urn:mrn:smart-navi:device:geo-server1', 'urn:mrn:smart-navi:device:mms1' , 'SP',now(),now()
union all
select 'urn:mrn:kr:vessel:neonexsoft:sc2', 'urn:mrn:smart-navi:device:mms2' , 'SC',now(),now()
union all
select 'urn:mrn:smart-navi:device:geo-server2', 'urn:mrn:smart-navi:device:mms2' , 'SP',now(),now()
union all
select 'urn:mrn:kr:vessel:neonexsoft:sc3', 'urn:mrn:smart-navi:device:mms3' , 'SC',now(),now()
union all
select 'urn:mrn:smart-navi:device:geo-server3', 'urn:mrn:smart-navi:device:mms3' , 'SP',now(),now();
