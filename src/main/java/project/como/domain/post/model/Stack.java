package project.como.domain.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Stack {
	JavaScript("JavaScript"),
	TypeScript("TypeScript"),
	React("React"),
	Vue("Vue"),
	Svelte("Svelte"),
	Nextjs("Nextjs"),
	Java("Java"),
	Spring("Spring"),
	Nodejs("Nodejs"),
	Nestjs("Nestjs"),
	Golang("Golang"),
	Kotlin("Kotlin"),
	Express("Express"),
	MySQL("MySQL"),
	MongoDB("MongoDB"),
	Python("Python"),
	Django("Django"),
	Php("Php"),
	GraphQL("GraphQL"),
	Firebase("Firebase"),
	Flutter("Flutter"),
	Swift("Swift"),
	ReactNative("ReactNative"),
	Unity("Unity"),
	AWS("AWS"),
	Kubernetes("Kubernetes"),
	Docker("Docker"),
	Git("Git"),
	Figma("Figma"),
	Zeplin("Zeplin"),
	Jest("Jest"),
	C("C");

	private String stack;
}
