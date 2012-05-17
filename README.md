toxbank-isa-plugin
==================

Plugin to search the Toxbank REST services within ISAcreator

Requirements:
------------------

* ToxBank Customized ISACreator, based on ISACreator 1.6.x

	* Note plugin interface had been changed since ISACreator 1.6.0, hence this plugin will not work in earlier ISACreator versions

	* The default ISACreator behaviour is to hide the investigation page, when only a single study is present. Since ToxBank custom fields are in the investigation page, the customized ToxBank ISACreator is compiled with an option to always show the investigation page in src/man/resources/defaultsettings.properties

     mvn clean assembly:assembly -P toxbank

	* ToxBank custom investigation.xml

