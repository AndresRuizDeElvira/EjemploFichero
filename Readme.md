1. Hola:
La aplicacion consigue entrar en dropbox, utilizando la DROPBOX API, listar los ficheros,
y mostrar el titulo del fichero correspondiente.En un menu spinner se selecciona ordenar por
Nombre o por fecha, y lo realiza una sola vez, una vez que se ha seleccionado no se puede volver
a seleccionar el menu.La parte cuarta esta implementada pero no funciona.Se descarga el fichero
a un directorio /data/data/Nombre del archivo pero al intentar abrirlo en SingleItemClass con un
epub reader falla y no he conseguido saber si esta descargado o no. Tambien he intentado utilizar 
la funcion de dropbox delta, pero tampoco he conseguido que corra, con lo que me he limitado a lanzar
las consultas via Dropbox API metadata. Importante: si intentais lanzar una consulta a Dropbox desde
el hilo principal fallará, en eso perdi dos dias, y aunque google considera que no esta bien he aplicado
el eliminador de policy error. Las partes principales de la aplicacion son:
1.AndroidListViewActivity.java
2.DownloadEpubFile.java
3.SingleListItem.java
Estan todas los mensajes System.out.println hacia el logcat, no me ha dado tiempo a eliminarlos...
