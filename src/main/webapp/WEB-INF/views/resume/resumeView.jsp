<%--
  Created by IntelliJ IDEA.
  User: yhwang131
  Date: 2017-06-21
  Time: 오전 9:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="decorator" uri="http://www.opensymphony.com/sitemesh/decorator" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<title>Profile</title>
	<!-- Noto Sans KR-Hestia -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/resume/NotoSansKR-Hestia.css">
	<!-- custom css -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/resume/resume.css">
</head>
<body>
	<div class="resume-wrapper">

		<!-- header -->
		<header class="resume">
			<h1 class="heading-container">Résumé</h1>
			<h2 class="name-container">your name</h2>
			<h3 class="job-container">your role</h3>
			<div class="profile-picture">
				<img src="${pageContext.request.contextPath}/resources/images/profile.png" alt="Profile">
			</div>
			<div class="contact-container">
				<ul class="contact-list">
					<li>
						<a href="tel:01000000000">+82. 010. 0000. 0000</a>
						<span><i class="fa fa-phone" aria-hidden="true"></i></span>
					</li>
					<li>
						<a href="mailto:your@email.com">your@email.com</a>
						<span><i class="fa fa-envelope-o" aria-hidden="true"></i></span>
					</li>
					<li>
						<a href="#" target="_blank">your homepage address</a>
						<span><i class="fa fa-globe" aria-hidden="true"></i></span>
					</li>
					<li>
						<a href="https://github.com/dhparkdh/resume-for-web-developer" target="_blank">your github address</a>
						<span><i class="fa fa-github" aria-hidden="true"></i></span>
					</li>
				</ul>
			</div>
		</header><!-- header -->

		<!-- main -->
		<section id="main">
			<div class="resume-item-wrapper">

				<!-- experince -->
				<section class="resume-item experience">
					<div class="inner">
						<h2>experience</h2>
						<ul>
							<!-- list example
							<li>
							  <h3>Your Role</h3>
							  <h4 class="company">Your Company Name & Location</h4>
							  <span class="date">Working Period</span>
							  <p class="detail">
								Details what you did at this company
							  </p>
							</li>
							-->
							<li>
								<h3>Chief Technology Officer</h3>
								<h4 class="company">Global Unicorn Corp. in Bundang</h4>
								<span class="date">Sept 2015 - present</span>
								<p class="detail">
									Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
								</p>
							</li>
							<li>
								<h3>Senior Front-End Developer</h3>
								<h4 class="company">Awesome Startup Corp. in Gangnam</h4>
								<span class="date">Feb 2015 - Jan 2016</span>
								<p class="detail">
									Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
								</p>
							</li>
							<li>
								<h3>Junior Front-End Developer</h3>
								<h4 class="company">Great Web Agency in Hongdae</h4>
								<span class="date">Mar 2010 - Jan 2015</span>
								<p class="detail">
									Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
								</p>
							</li>
						</ul>
					</div>
				</section><!-- experince -->

				<!-- project -->
				<section class="resume-item project">
					<div class="inner">
						<h2>project</h2>
						<ul>
							<!-- list example
							<li>
							  <h3><a href="#" target="_blank">Your Project Name</a></h3>
							  <p class="detail">
								Details what the project is
							  </p>
							  <p class="tags">#hash #tags</p>
							</li>
							-->
							<li>
								<h3><a href="https://github.com/dhparkdh/resume-for-web-developer" target="_blank">HTML5 Résumé template for web developer</a></h3>
								<p class="detail">
									Online Résumé(CV) template for web developer using HTML5 and CSS3.
								</p>
								<p class="tags">#HTML5 #CSS3 #Noto</p>
							</li>
							<li>
								<h3><a href="#" target="_blank">Project Name</a></h3>
								<p class="detail">
									Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
								</p>
								<p class="tags">#hash #tags</p>
							</li>
							<li>
								<h3><a href="#" target="_blank">Project Name</a></h3>
								<p class="detail">
									Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
								</p>
								<p class="tags">#hash #tags</p>
							</li>
						</ul>
					</div>
				</section><!-- project -->

				<!-- skill -->
				<section class="resume-item skill">
					<div class="inner">
						<h2>Skill</h2>
						<ul>
							<!-- list example
							<li>
							  <h3>Your Skill Name</h3>
							  <div class="skill-bar">
								<span class="bar" style="width: Percentage% ;"></span>
							  </div>
							</li>
							-->
							<li>
								<h3>Master</h3>
								<div class="skill-bar">
									<span class="bar" style="width: 100% ;"></span>
								</div>
							</li>
							<li>
								<h3>Professional</h3>
								<div class="skill-bar">
									<span class="bar" style="width: 80% ;"></span>
								</div>
							</li>
							<li>
								<h3>Senior</h3>
								<div class="skill-bar">
									<span class="bar" style="width: 60% ;"></span>
								</div>
							</li>
							<li>
								<h3>Junior</h3>
								<div class="skill-bar">
									<span class="bar" style="width: 40% ;"></span>
								</div>
							</li>
							<li>
								<h3>Rookie</h3>
								<div class="skill-bar">
									<span class="bar" style="width: 20% ;"></span>
								</div>
							</li>
						</ul>
					</div>
				</section><!-- skill -->

			</div>
		</section><!-- main -->

		<!-- footer -->
		<footer>
			© 2016 <a href="https://github.com/dhparkdh" target="-_blank">dhpark</a>. All rights reserved.
		</footer><!-- footer -->

	</div>
</body>
</html>
