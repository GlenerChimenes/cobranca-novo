package com.algaworks.cobranca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.algaworks.cobranca.model.StatusTitulo;
import com.algaworks.cobranca.model.Titulo;
import com.algaworks.cobranca.repository.TituloFilter;
import com.algaworks.cobranca.repository.TituloRepository;

@Service
public class CadastroTituloService  {

	@Autowired
	private TituloRepository tituloRepository;
	
	public void salvar(Titulo titulo) {
		try{
		tituloRepository.save(titulo);
		
		}catch(DataIntegrityViolationException e) {
			throw new IllegalArgumentException("Formato de data inválido");
		}
		}

	public void excluir(Titulo titulo) {
		tituloRepository.delete(titulo);
	}

	public String receber(Long codigo) {
		Titulo titulo = tituloRepository.findByCodigo(codigo);
		titulo.setStatus(StatusTitulo.RECEBIDO);
		tituloRepository.save(titulo);
		
		return StatusTitulo.RECEBIDO.getDescricao();
	}
	
	public List<Titulo> filtrar(TituloFilter filtro){
		String descricao = filtro.getDescricao() == null ? "%" : filtro.getDescricao();
		return  tituloRepository.findByDescricaoContaining(descricao);
	}
	
	
}
