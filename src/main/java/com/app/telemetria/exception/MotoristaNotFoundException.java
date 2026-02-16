package com.app.telemetria.exception;

	public class MotoristaNotFoundException extends RuntimeException {
		public MotoristaNotFoundException(Long id) {
			super("Motorista nao encontrado com id:" + id);
		}
		
		public MotoristaNotFoundException(String cpf) {
			super("Motorista nao encontrado com CPF:" + cpf);
		}
	}
	
