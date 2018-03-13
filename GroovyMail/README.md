smtp service by groovy based on javamail
===
this project use javamail to send e-mail,<br>
but use groovy to make it easier,
so now you can use ** Domain Specific Language ** to use it

###here is an example for quick start

```Groovy
def params=[contact:"",choosetime:"",hour:"",minute:""]
new Mail(this).sendMail {
    from "somemail"
	to "somemail"
	subject "subject"
	message """
     contact method：${params.contact}
	 time：${params.choosetime}  ${params.hour}时${params.minute}分
	"""
    send username:"some mail username",password:"your smtp passsword provided by the
    smtp server
    "
	}

```

if you are use groovy as script,you should use <span style="color:red">Mail(Object)</span> to create a mail object
<br>constructor to create a object by this which references the script object 
<br>
so as to use the groovy script variable
***
all the other method can be found in [com.jiayang.GroovyMail.MailResolver](src/main/groovy/com/jiayang/GroovyMail/MailResolver),this class
define all the method you can use in sendMail's Closure
***
pay attention,you can use send without any params whether you have called ***username method***
and ***password method*** before use send,you can also send attachment only by  using a File Object call message
method as its parameter.