#!/bin/sh
#env
APP_NAME="ch.user.web"

#set base home
RESOURCES_HOME=${CATALINA_HOME}/webapps/ROOT/WEB-INF/classes

#change config
pushd ${RESOURCES_HOME}
sed -i "s%casServerLoginUrl=.*%casServerLoginUrl=${casServerLoginUrl}%g" ./sso.properties
sed -i "s%casServerUrlPrefix=.*%casServerUrlPrefix=${casServerUrlPrefix}%g" ./sso.properties
sed -i "s%serverName=.*%serverName=${serverName}%g" ./sso.properties
sed -i "s%logOutServerUrl=.*%logOutServerUrl=${logOutServerUrl}%g" ./sso.properties
sed -i "s%logOutBackUrl=.*%logOutBackUrl=${logOutBackUrl}%g" ./sso.properties
sed -i "s%casServerLoginUrl_Inner=.*%casServerLoginUrl_Inner=${casServerLoginUrl_Inner}%g" ./sso.properties
sed -i "s%casServerUrlPrefix_Inner=.*%casServerUrlPrefix_Inner=${casServerUrlPrefix_Inner}%g" ./sso.properties
sed -i "s%serverName_Inner=.*%serverName_Inner=${serverName_Inner}%g" ./sso.properties
sed -i "s%logOutServerUrl_Inner=.*%logOutServerUrl_Inner=${logOutServerUrl_Inner}%g" ./sso.properties
sed -i "s%logOutBackUrl_Inner=.*%logOutBackUrl_Inner=${logOutBackUrl_Inner}%g" ./sso.properties
sed -i "s%innerDomains=.*%innerDomains=${innerDomains}%g" ./sso.properties

sed -i "s%whiteList=.*%whiteList=${whiteList}%g" ./whitelist.properties

sed -i "s%paas.sdk.mode=.*%paas.sdk.mode=${SDK_MODE}%g" ./paas/paas-conf.properties
sed -i "s%ccs.appname=.*%ccs.appname=${CCS_NAME}%g" ./paas/paas-conf.properties
sed -i "s%ccs.zk_address=.*%ccs.zk_address=${ZK_ADDR}%g" ./paas/paas-conf.properties

sed -i "s%findByCompanyId_http_url=.*%findByCompanyId_http_url=${findByCompanyId_http_url}%g" ./httpUrl.properties
sed -i "s%searchCompanyList_http_url=.*%searchCompanyList_http_url=${searchCompanyList_http_url}%g" ./httpUrl.properties
sed -i "s%updateAuditState_http_url=.*%updateAuditState_http_url=${updateAuditState_http_url}%g" ./httpUrl.properties
sed -i "s%updateCompanyState_http_url=.*%updateCompanyState_http_url=${updateCompanyState_http_url}%g" ./httpUrl.properties

sed -i "s%balance_http_url=.*%balance_http_url=${balance_http_url}%g" ./httpUrl.properties
sed -i "s%paymentApplication_http_url=.*%paymentApplication_http_url=${paymentApplication_http_url}%g" ./httpUrl.properties
sed -i "s%appkey=.*%appkey=${appkey}%g" ./httpUrl.properties

sed -i "s%sigh_classpath=.*%sigh_classpath=${sigh_classpath}%g" ./httpUrl.properties
sed -i "s%check_sign_classpath=.*%check_sign_classpath=${check_sign_classpath}%g" ./httpUrl.properties
sed -i "s%paymentOrder_http_url=.*%paymentOrder_http_url=${paymentOrder_http_url}%g" ./httpUrl.properties

# 各中心要根据情况自己修改成与dubbo.properties中对应的配置项
sed -i "s%dubbo.registry.address=.*%dubbo.registry.address=${REST_REGISTRY_ADDR}%g" ./dubbo.properties
popd

nohup ${CATALINA_HOME}/bin/catalina.sh run >> /${APP_NAME}.log