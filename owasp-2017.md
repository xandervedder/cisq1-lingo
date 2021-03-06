# Vulnerability Analysis

## A1:2017 Injection

### Description
Injection occurs when untrusted data (unsanitized) is sent to an interpreter as part of a command or query. 
The attacker's hostile data can trick the interpreter into executing unintend commands or accesing data without proper authorization.

### Risk
There is somewhat of a risk in this project, since we are using a third party ORM solution, Hibernate. 
We would have to trust that Hibernate mitigates all attack vectors possible.

Authentication could somewhat reduce the aforementioned risk, but authentication/authorization is also dependant on third party solutions.

### Counter-measures
Since we are using third party solutions, it is **very** important to keep the dependencies up to date. 

In this case we got ourselves covered by using [dependabot](https://dependabot.com/).

## A2:2017 Broken Authentication

### Description
Application functions related to authentication and session management are often implemented incorrectly, 
allowing attackers to compromise passwords, keys, or session tokens, 
or to exploit other implementation flaws to assume other users’ identities temporarily or permanently.

### Risk
Currently, there is no authentication in this project. For this project, that is fine. 
If we would be using authentication in this project, we would again be relying on third party solutions (Spring in this case).

### Counter-measures
The counter measures are the same for the [injection](#a12017-injection), but since a part of this project is a REST api, 
there would be no need to worry about session managment, since a REST api is stateless.

## A3:2017 Sensitive Data Exposure

### Description
Many web applications and APIs do not properly protect sensitive data, such as financial, healthcare, and PII. 
Attackers may steal or modify such weakly protected data to conduct credit card fraud, identity theft, or other crimes.
Sensitive data may be compromised without extra protection, such as encryption at rest or in transit, 
and requires special precautions when exchanged with the browser.

### Risk
There's no sensitive data in this project currently, this story would change if authentication/authorization would be introduced (passwords).

### Counter-measures
If authentication/authorization would be present, hashing the password would be a very important thing to do. Not exposing entire
domain objects would also be smart, instead, use Dto's (Data Transfer Objects) where possible.

## A4:2017 XML External Entities

### Description
Many older or poorly configured XML processors evaluate external entity references within XML documents. 
External entities can be used to disclose internal files using the file URI handler, internal file shares, 
internal port scanning, remote code execution, and denial of service attacks.

### Risk
There is no risk involved here, even with authorization/authentication.

### Counter-measures
Not needed.

## A5:2017 Broken Access Control

### Description
Restrictions on what authenticated users are allowed to do are often not properly enforced. 
Attackers can exploit these flaws to access unauthorized functionality and/or data, such as access other users’ accounts,
view sensitive files, modify other users’ data, change access rights, etc.

### Risk
No risk at the moment, since the API is completely open at the moment (without sensitive data available). 
This would change when authorization/authentication is added.

### Counter-measures
Prevent autenticated users from accessing other users' resources and prevent non-authenticated users from accessing any resources.

## A6:2017 Security Misconfiguration

### Description
Security misconfiguration is the most commonly seen issue. This is commonly a result of insecure default configurations,
incomplete or ad hoc configurations, open cloud storage, misconfigured HTTP headers,
and verbose error messages containing sensitive information.
Not only must all operating systems, frameworks, libraries, and applications be securely configured, 
but they must be patched/upgraded in a timely fashion.

### Risk
There is little risk here, but again, we are mostly relying on third party solutions. 
In this instance we are using a Spring boot starter configuration, which could contain misconfigurations.

### Counter-measures
Update dependencies timely.

## A7:2017 Cross Site Scripting (XXS)

### Description
XSS flaws occur whenever an application includes untrusted data in a new web page without proper validation or escaping,
or updates an existing web page with user-supplied data using a browser API that can create HTML or JavaScript.
XSS allows attackers to execute scripts in the victim’s browser which can hijack user sessions, deface web sites,
or redirect the user to malicious sites.

### Risk
There is no risk involved here, even with authorization/authentication. 

However, I do plan on building a front-end for this project. But even then, XXS probably isn't possible.

### Counter-measures
Not needed.

## A8:2017 Insecure Deserialization

### Description
Insecure deserialization often leads to remote code execution. Even if deserialization flaws do not result in remote code
execution, they can be used to perform attacks, including replay attacks, injection attacks, and privilege escalation attacks.

### Risk
In this case there is a small risk since we are using a third party json serializer and deserializer, jackson.

### Counter-measures
Like mentioned before, keep dependencies up to date.

## A9:2017 Using Components with Known Vulnerabilities

### Description
Components, such as libraries, frameworks, and other software modules, run with the same privileges as the application.
If a vulnerable component is exploited, such an attack can facilitate serious data loss or server takeover. 
Applications and APIs using components with known vulnerabilities may undermine application defenses and enable various attacks and impacts.

### Risk
Like we've mentioned in previous risk assessments, the risk is always there when using third party solutions/plugins. 
The same goes for this vulnerability.

The risk increases when specifically using third party authentication/authorization solutions.

### Counter-measures
As mentioned before, using something like dependabot would be ideal. Dependabot scans for vulnerabilities and bumps versions automatically.

## A10:2017 Insufficient Logging & Monitoring

### Description
Insufficient logging and monitoring, coupled with missing or ineffective integration with incident response, 
allows attackers to further attack systems, maintain persistence, pivot to more systems, and tamper, extract, or destroy data. 
Most breach studies show time to detect a breach is over 200 days, typically detected by external parties rather than internal
processes or monitoring.

### Risk
There is certainly a risk here, since we (aside from Springs logging) do not log anything in our Controllers and Services.

### Counter-measures
In this case we could simply add logging in our Controllers and Services, but it could also be helpful to add logging
when something goes wrong (i.e. Exceptions).
