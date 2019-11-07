-- delete test data for geocasting
delete from ENTITY_HOME_MMS
where MRN_MMS in ('urn:mrn:kr:device:neonexsoft:mms1','urn:mrn:kr:device:neonexsoft:mms2','urn:mrn:kr:device:neonexsoft:mms3');
delete from MMS
where MRN in ('urn:mrn:kr:device:neonexsoft:mms1','urn:mrn:kr:device:neonexsoft:mms2','urn:mrn:kr:device:neonexsoft:mms3');


-- insert test data for geocasting
insert into MMS(MRN, URL, CREATE_DATE, UPDATE_DATE)
select 'urn:mrn:kr:device:neonexsoft:mms1', 'http://192.168.81.57:8088' ,now(),now()
union all
select 'urn:mrn:kr:device:neonexsoft:mms2', 'http://192.168.81.83:8088',now(),now()
union all
select 'urn:mrn:kr:device:neonexsoft:mms3',  'http://192.168.81.43:8088',now(),now();

insert into ENTITY_HOME_MMS(MRN, MRN_MMS, TYPE, CREATE_DATE, UPDATE_DATE)
select 'urn:mrn:kr:vessel:neonexsoft:sc1', 'urn:mrn:kr:device:neonexsoft:mms1', 'SC'  ,now(),now()
union all
select 'urn:mrn:kr:service:instance:neonexsoft:sp1', 'urn:mrn:kr:device:neonexsoft:mms1' , 'SP',now(),now()
union all
select 'urn:mrn:kr:vessel:neonexsoft:sc2', 'urn:mrn:kr:device:neonexsoft:mms2' , 'SC',now(),now()
union all
select 'urn:mrn:kr:service:instance:neonexsoft:sp2', 'urn:mrn:kr:device:neonexsoft:mms2' , 'SP',now(),now()
union all
select 'urn:mrn:kr:vessel:neonexsoft:sc3', 'urn:mrn:kr:device:neonexsoft:mms3' , 'SC',now(),now()
union all
select 'urn:mrn:kr:service:instance:neonexsoft:sp3', 'urn:mrn:kr:device:neonexsoft:mms3' , 'SP',now(),now();
