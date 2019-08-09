# WoongJin-AI
웅진 독해 AI 서비스 개발

- Activity

1. MainActivity
: home
select script to learn


2. LoginActivity
: login
-> home


3. RegisterActivity
: register
-> login


4. SelectTypeActivity
: select type of quiz
1) OX type 2) choice type 3) short word type
-> 1) OXTypeActivity 2) ChoiceTypeActivity 3) ShortwordTypeActivity


5. OXTypeActivity
: make OX type quiz
-> 


6. ChoiceTypeActivity
: make choice type quiz
-> 


7. ShortwordTypeActivity
: make short word type quiz
-> 


8. TemplateActivity
: show templates of each quiz type
1 random template of 3 most favorite questions



- Fragment




- Firebase class

1. UserInfo
: user info (id, name, pw)


2. QuizOXShortwordTypeInfo
: OX type, short word type quiz info (uid, question, answer, star, desc, like)


3. QuizChoiceTypeInfo
: choice type quiz info (uid, question, answer, answer1, answer2, answer3, answer4, star, desc, like)

