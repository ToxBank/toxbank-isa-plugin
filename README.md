toxbank-isa-plugin
==================

This is a plugin to search the [ToxBank](http://www.toxbank.net) REST services within [ISAcreator](http://isatools.wordpress.com/tag/isacreator/).

The plugin in this branch is updated to work with ISACreator 1.7.x. The ISACreator 1.6.x plugin is moved to 
[PluginForISACreator1.6.2](https://github.com/ToxBank/toxbank-isa-plugin/tree/PluginForISACreator1.6.2) branch.

## References

* The ToxBank Warehouse [publication](http://onlinelibrary.wiley.com/doi/10.1002/minf.201200114/abstract)
* [ToxBank API](http://api.toxbank.net/index.php)

ISACreator.SEURAT 
-----------------

* ISACreator.SEURAT is based on ISACreator 1.6.x, customized for [ToxBank](http://ww.toxbank.net)

* [Download ISACreator.SEURAT 1.6.2 Windows installer](http://www.ideaconsult.net/downloads/ISAcreator.SEURAT/ISAcreator.SEURAT-v1.6.2-setup.exe)

* [Download ISACreator.SEURAT 1.6.2 OS independent zip archive](http://www.ideaconsult.net/downloads/ISAcreator.SEURAT/ISAcreator.SEURAT-v1.6.2.zip)


Plugin requirements:
------------------

* ISACreator.SEURAT  

* Note the plugin interface had been changed since ISACreator 1.6.0, hence this plugin will not work in earlier ISACreator versions

* The default ISACreator behaviour is to hide the investigation page, when only a single study is present. Since ToxBank custom fields are in the investigation page, the customized ToxBank ISACreator is compiled with an option to always show the investigation page in src/man/resources/defaultsettings.properties

* ToxBank custom [investigation.xml](https://github.com/ToxBank/isa2rdf/blob/master/isa2rdf/isa2rdf-cli/src/main/resources/toxbank-config/investigation.xml)

Build :
------------------

     mvn clean assembly:assembly -P toxbank

Screenshot :
------------------

<img src='screenshot.png'>


